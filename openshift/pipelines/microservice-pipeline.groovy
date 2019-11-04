node("maven") {

	properties([
	  parameters([
	     string(name: 'GITHUB'), string(name: 'PROJECT'),
	  ])
	])

	def project = params.GITHUB
	def microservice = "project-burndown"	
	def version

	stage("checkout") {
		version = sh(returnStdout: true, script: "git tag --sort version:refname | tail -1").trim()
		git branch: version, url: "https://github.com/${params.GITHUB}/estafet-microservices-scrum-api-project-burndown"
	}
	
	stage("prepare the database") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean package -P prepare-db -Dmaven.test.skip=true -Dproject=${project}"
	    } 
	}

	stage("create build config") {
			sh "oc process -n ${project} -f openshift/templates/${microservice}-build-config.yml -p NAMESPACE=${project} -p GITHUB=${params.GITHUB} -p DOCKER_IMAGE_LABEL=${version} SOURCE_REPOSITORY_REF=${version} | oc apply -f -"
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

}

