node("maven") {

	def project = "dev"
	def microservice = "project-burndown"

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-project-burndown"
	}

	stage("build and execute unit tests") {
		sh "mvn clean test"
		post {
			always {
				junit "**/target/surefire-reports/*.xml"
			}
		}
	}

	stage("update the database schema") {
		sh "oc get pods --selector app=postgresql -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n dev"
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/drop-${microservice}-db.ddl\""
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/create-${microservice}-db.ddl\""
	}

	stage("build & deploy container") {
		openshiftBuild namespace: ${project}, buildConfig: ${microservice}, showBuildLogs: "true",  waitTime: "3000000"
	}
  	  
	stage("verify container deployment") {
		openshiftVerifyDeployment namespace: ${project}, depCfg: ${microservice}, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"	
	}

	stage("execute the container tests") {
		withEnv(
			[	"PROJECT_BURNDOWN_REPOSITORY_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${microservice}", 
				"PROJECT_BURNDOWN_REPOSITORY_DB_USER=postgres", 
				"PROJECT_BURNDOWN_REPOSITORY_DB_PASSWORD=welcome1",
				"PROJECT_BURNDOWN_SERVICE_URI=http://${microservice}.${project}-scrum.svc:8080",
				"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
				"JBOSS_A_MQ_BROKER_USER=amq",
				"JBOSS_A_MQ_BROKER_PASSWORD=amq"
			]) {
			sh "mvn verify -P integration-test"
		}
		post {
			always {
				junit "**/target/failsafe-reports/*.xml"
			}
		}
	}

}

