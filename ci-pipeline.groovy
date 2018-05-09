node("maven") {

	def project = "dev"
	def microservice = "project-burndown"

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-project-burndown"
	}
	
/*
	stage("unit tests") {
		try {
			sh "mvn clean test"
		} finally {
			junit "**/target/surefire-reports/*.xml"
		}
	}
*/

	stage("update database") {
		sh "oc get pods --selector app=postgresql -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n ${project}"
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/drop-${microservice}-db.ddl\""
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/create-${microservice}-db.ddl\""
	}
	
	stage("update wiremock") {
		sh "oc get pods --selector app=wiremock-docker -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc exec ${pod} -n ${project} -- /bin/sh -i -c \"rm -f /home/wiremock/mappings/*.json\""
		sh "oc rsync src/integration-test/resources/mappings/ ${pod}:/home/wiremock/mappings -n ${project}"
		openshiftDeploy namespace: project, depCfg: "wiremock-docker", showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "wiremock-docker", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}

	stage("reset a-mq to purge topics") {
		openshiftDeploy namespace: project, depCfg: "broker-amq", showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "broker-amq", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}

	stage("build & deploy container") {
		openshiftBuild namespace: project, buildConfig: microservice, showBuildLogs: "true",  waitTime: "300000"
		sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616 -n ${project}"
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "600000"
	}

	stage("container tests") {
		try {
			withEnv(
				[	"PROJECT_BURNDOWN_REPOSITORY_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${microservice}", 
					"PROJECT_BURNDOWN_REPOSITORY_DB_USER=postgres", 
					"PROJECT_BURNDOWN_REPOSITORY_DB_PASSWORD=welcome1",
					"PROJECT_BURNDOWN_SERVICE_URI=http://${microservice}.${project}.svc:8080",
					"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
					"JBOSS_A_MQ_BROKER_USER=amq",
					"JBOSS_A_MQ_BROKER_PASSWORD=amq"
				]) {
					sh "mvn verify -P integration-test"
				}
			} finally {
				sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616 -n ${project}"
				junit "**/target/failsafe-reports/*.xml"
			}
	}
	
	stage("tag container for testing") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'latest', destinationNamespace: 'test', destinationStream: microservice, destinationTag: 'PrepareForTesting'
	}

}

