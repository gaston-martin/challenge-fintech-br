#!/bin/bash


source commons.sh

fStopIfRunningPrev(){
  for CONTAINER in $(docker ps -a -q --filter="name=database")
  do
    echo "${RED}Found a previous instance of the container (${CONTAINER})${WHITE}"
    docker ps --filter="id=$CONTAINER"
    echo "${BOLD}Stopping... ${NORMAL}"
    docker stop ${CONTAINER}
  done
}

fCheckFile docker-compose.yml
fCheckCommand docker
fStopIfRunningPrev

echo -e "\n${BOLD}Starting new database container...${NORMAL}"
docker compose run -d --name database --rm -P database || fError "docker compose returned error"

docker ps --filter="name=database"
