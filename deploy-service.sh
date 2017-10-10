mvn clean install -P local
cp target/estafet-microservices-scrum-api-project-burndown-*.war $WILDFLY_INSTALL/standalone/deployments
