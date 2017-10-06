# Estafet Microservices Scrum Project Burndown Report
Aggregates data from various events to produce project burndown reports. Each time a sprint has concluded, the project burndown is updated.
## What is this?
This application is a microservice that produces a burndown report for a project when supplied with a project id.

Each microservice has it's own git repository, but there is a master git repository that contains links to all of the repositories [here](https://github.com/Estafet-LTD/estafet-microservices-scrum).
## Getting Started
You can find a detailed explanation of how to install this (and other microservices) [here](https://github.com/Estafet-LTD/estafet-microservices-scrum#getting-started).
## API Interface

The project burndown JSON object

```json
{
    "id": 1,
    "sprints": [
        {
            "id": null,
            "number": 0,
            "pointsTotal": 46,
            "projectId": null
        },
        {
            "id": 1,
            "number": 1,
            "pointsTotal": 41,
            "status": "Completed",
            "projectId": 1
        }
    ]
}
```

To retrieve an example the project burndown object (useful for testing to see the microservice is online).

```
GET http://project-burndown/burndown
```

To retrieve a project burndown a project that has an id = 4

```
GET http://project-burndown/project/4/burndown
```

## Environment Variables
```
JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616
JBOSS_A_MQ_BROKER_USER=estafet
JBOSS_A_MQ_BROKER_PASSWORD=estafet

PROJECT_BURNDOWN_REPOSITORY_JDBC_URL=jdbc:postgresql://localhost:5432/project-burndown
PROJECT_BURNDOWN_REPOSITORY_DB_USER=postgres
PROJECT_BURNDOWN_REPOSITORY_DB_PASSWORD=welcome1

STORY_REPOSITORY_SERVICE_URI=http://localhost:8080/story-repository
PROJECT_REPOSITORY_SERVICE_URI=http://localhost:8080/project-repository
```

