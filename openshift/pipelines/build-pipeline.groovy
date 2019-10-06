@NonCPS
def getVersion(pom) {
	def matcher = new XmlSlurper().parseText(pom).version =~ /(\d+\.\d+\.)(\d+)(\-SNAPSHOT)/
	return "${matcher[0][1]}${matcher[0][2].toInteger()}-SNAPSHOT"
}

node("maven") {

	def project = "build"
	def microservice = "project-burndown"	
	def version

	currentBuild.description = "Build a container from the source, then execute unit and container integration tests before promoting the container as a release candidate for acceptance testing."

	properties([
	  parameters([
	     string(name: 'GITHUB'),
	  ])
	])

	stage("checkout") {
		git branch: "master", url: "https://github.com/${params.GITHUB}/estafet-microservices-scrum-api-project-burndown"
	}

	stage("unit tests") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean test"
	    } 
	}
	
	stage("prepare the database") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean package -P prepare-db -Dmaven.test.skip=true -Dproject=${project}"
	    } 
	}
	
	stage("reset a-mq to purge topics") {
		openshiftDeploy namespace: project, depCfg: "broker-amq", showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "broker-amq", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}
	
	stage("reset the promoted image stream") {
		def pom = readFile('pom.xml')
		version = getVersion(pom)
		sh "oc tag -d ${microservice}:${version} -n cicd || true"
	}	
	
	stage("create build config") {
			sh "oc process -n ${project} -f openshift/templates/${microservice}-build-config.yml -p NAMESPACE=${project} -p GITHUB=${params.GITHUB} -p DOCKER_IMAGE_LABEL=${version} | oc apply -f -"
	}

	stage("execute build") {
		openshiftBuild namespace: project, buildConfig: microservice, waitTime: "300000"
		openshiftVerifyBuild namespace: project, buildConfig: microservice, waitTime: "300000" 
	}

	stage("create deployment config") {
		sh "oc process -n ${project} -f openshift/templates/${microservice}-config.yml -p NAMESPACE=${project} -p DOCKER_NAMESPACE=${project} -p DOCKER_IMAGE_LABEL=${version} | oc apply -f -"
	}

	stage("execute deployment") {
		openshiftDeploy namespace: project, depCfg: microservice,  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000" 
	}

	stage("execute the container tests") {
			withEnv(
				[	"PROJECT_BURNDOWN_REPOSITORY_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${project}-${microservice}", 
					"PROJECT_BURNDOWN_REPOSITORY_DB_USER=postgres", 
					"PROJECT_BURNDOWN_REPOSITORY_DB_PASSWORD=welcome1",
					"PROJECT_BURNDOWN_SERVICE_URI=http://${microservice}.${project}.svc:8080",
					"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
					"JBOSS_A_MQ_BROKER_USER=amq",
					"JBOSS_A_MQ_BROKER_PASSWORD=amq"
				]) {
			withMaven(mavenSettingsConfig: 'microservices-scrum') {
					try {
						sh "mvn clean verify -P integration-test"
					} finally {
						sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616 -n ${project}"
					}
			}
		}
	}

	stage("deploy snapshots") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
 			sh "mvn clean deploy -Dmaven.test.skip=true"
		} 
	}	
	
	stage("promote the image") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: version, destinationNamespace: 'cicd', destinationStream: microservice, destinationTag: version
	}

}

