# How to run the service

This service has been developed using Java 21, Gradle, and some standard dependencies stated in gradle's configuration.
It can be executed by building and running the Dockerfile, or as a standalone Spring web server

The database has been configured to run as a docker container, it's specified in docker-compose.yml
This setup creates a separate volume named "mariadb" in which all the database data is being kept. 
So that any data created by the service will survive reboots or bounces of the database container. See "Reset Database" 
below for instructions on how to clear the database.

## (A) Command Line

1. Go to the project root folder
2. Make sure your JAVA_HOME is pointing to a valid java 21 SDK 
   * `echo $JAVA_HOME`
   * `java -version`
3. Run `./run_service_standalone.sh`
4. To stop the service press CTRL-C in the terminal running the service

The script will **detect** the termination of the program and therefore will **stop** and **remove** the database container

## (B) Fully Containerized

1. Go to the project root folder
2. Run `./run_service_in_docker.sh`
3. To stop the service press CTRL-C in the terminal running the service

As both the database and the service runs within docker containers the output of the service will be mixed out

The `docker-compose.yml` file has been configured to wait for the database to be **ready** before starting the REST service.

After stopping the services, no container should be kept running. 
Bear in mind that the database runs over a separate docker volume so any data written to the database will survive subsequent container bounces.
See notes below for cleaning up the database.


## Notes

### Reset Database

In order to reset the database to an initial state, the docker volume named "mariadb" must be deleted. 
To delete the volume, it must be out of use. So you need to stop and delete the database container first

1. Identify and stop the database container
```
$ docker ps

➜  Documents docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED         STATUS         PORTS                     NAMES
91de2d9280ad   mariadb   "docker-entrypoint.s…"   6 seconds ago   Up 5 seconds   0.0.0.0:33067->3306/tcp   database
$ docker stop database
```

2. Prune all inactive containers
```
$ docker container prune -f
# You can also remove the specific container with issuing docker ps -a and then docker container rm ID  
```

3. List the docker volumes
```
$ docker volume ls
DRIVER    VOLUME NAME
local     challenge-fintech-br_mariadb
```

4. Remove the volume
```
$ docker volume rm challenge-fintech-br_mariadb
```


