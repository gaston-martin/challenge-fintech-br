# challenge-fintech-br
Challenge project in Java for a fintech company

## Requirements
[Requirements of the Challenge](doc/CHALLENGE.md)

## Prerequisites
[Setup and prerequisites](doc/SETUP.md)

## Instructions to run the service
[How to run the service](doc/EXECUTION.md)

## Sample requests
[Sample requests for all endpoints](doc/REQUESTS.md)

## Time tracking
[Summary of tasks performed](doc/TIMETRACKING.md)

## Author

| Name           | Email             |
|----------------|-------------------|
| Gaston Martin  | gastonm@gmail.com |

## Disclaimers

- This project has been developed and tested in a MAC computer running OSX with Docker Desktop
- Due to a config issue with timezone, when running the application in a docker container, the localtime of the container 
may differ from the real localtime. Hence the endpoint for querying the balance at some point in time might misbehave.
I discovered this issue at the latest minutes, but run out of time to troubleshoot the Alpine TZ configuration of the container.
