# Setup and prerequisites

Prerequisites

| Prerequisite | Instructions                                                                                             |
|--------------|----------------------------------------------------------------------------------------------------------|
| (sdkman)     | Optional: Install sdkman from https://sdkman.io/ to manage installed JDKs                                |
| Docker       | Install as advised in https://www.docker.com/. For Mac you can use Docker Desktop                        |
| Java 21      | Required to run the project outside of docker. It's strongly recommended to use sdkman for installing it |


Instructions

1. Java in already installed inside the docker image used for building the project. For running the project outside of docker, setup **JAVA_HOME** to point to the location of JDK21. If using sdkman, run the following
    `sdk use java 21.0.6-amzn` using the specific version you have installed (replace 21.0.6-amzn with your version)
2. This project requires an active connection to internet for downloading some needed artifacts.
    - Latest published version of gradle
    - Project dependencies (Spring, Spring-web, Spring-data-jpa, Mariadb JDBC Driver, and so on)

