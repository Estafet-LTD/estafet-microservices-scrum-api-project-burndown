# Estafet Microservices Scrum Project Burndown Report
Aggregates data from various events to produce project burndown reports. Each time a sprint has concluded, the project burndown is updated.
## What is this?
This application is a microservice that produces a burndown report for a project when supplied with a project id.

Each microservice has it's own git repository, but there is a master git repository that contains links to all of the repositories [here](https://github.com/Estafet-LTD/estafet-microservices-scrum).
## Getting Started
You can find a detailed explanation of how to install this (and other microservices) [here](https://github.com/Estafet-LTD/estafet-microservices-scrum#getting-started).
## API Interface

|Topic               |Direction |Event                                                                                 |Message Type       |
|--------------------|----------|--------------------------------------------------------------------------------------|-------------------|
|new.project.topic   |Consumer  |When a project is created, a new burndown for that project is initialised             |Project JSON Object|
|new.sprint.topic    |Consumer  |When a sprint is created, the project burn down will be updated                       |Project JSON Object|
|new.story.topic     |Consumer  |When a story is created, the points total for the existing sprint will be incremented |Project JSON Object|
|update.story.topic  |Consumer  |When a story is updated, the points total for the existing sprint will be incremented |Project JSON Object|
|update.sprint.topic |Consumer  |When a sprint is updated, the points total for the existing sprint will be incremented|Project JSON Object|

### Project Burndown JSON object

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

### Restful Operations

To retrieve an example the project burndown object (useful for testing to see the microservice is online).

```
GET http://<project-burndown-uri>/api
```

To retrieve a project burndown a project that has an id = 4

```
GET http://<project-burndown-uri>/project/4/burndown
```

## Environment Variables
```
JBOSS_A_MQ_BROKER_URL
JBOSS_A_MQ_BROKER_USER
JBOSS_A_MQ_BROKER_PASSWORD

PROJECT_BURNDOWN_REPOSITORY_JDBC_URL
PROJECT_BURNDOWN_REPOSITORY_DB_USER
PROJECT_BURNDOWN_REPOSITORY_DB_PASSWORD
```

