# Setup and prerequisites

## Prerequisites

| Prerequisite  | Instructions                                                                                                       |
|---------------|--------------------------------------------------------------------------------------------------------------------|
| (Sdkman)      | Optional: Install sdkman from https://sdkman.io/ to manage (install, select) JDKs                                  |
| Docker        | Install as advised in https://www.docker.com/. For Mac you can use Docker Desktop                                  |
| (Java SDK 21) | Required to build and run the project outside of docker. It's strongly recommended to use sdkman for installing it |
| Internet      | This project requires an active connection to internet for downloading some needed artifacts                       | 


## Notes:

Amazon's Corretto JDK21 is already installed inside the docker image used for building the project. 

For running the project outside of docker, setup **JAVA_HOME** first to point to the location of JDK21. 

If you are using sdkman, run the following:

    `sdk use java 21.0.6-amzn` 

but using the specific version you have already installed (replace 21.0.6-amzn with your version)

This project will use internet for downloading some required artifacts:

- Latest published version of gradle
- Project dependencies (Spring, Spring-web, Spring-data-jpa, Mariadb JDBC Driver, and so on)

Installing gradle is not required, as the project uses Gradle Wrapper to download the latest version for your platform 