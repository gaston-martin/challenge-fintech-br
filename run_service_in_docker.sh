#!/bin/bash


source commons.sh


fEditConfig(){
  CONFIG_FILE="src/main/resources/application.properties"
  [ -f "$CONFIG_FILE" ] || fError "Unable to find config file $CONFIGFILE"

  # This is needed because localhost is local to container when run in docker
  # But there is no standard and portable way of pointing to host's network from within container
  # (what works in linux doesn't work in mac osx)
  # So when running in docker, the service name is used as the alias is configured by docker compose
  # and the port used is the container port, not the host-mapped port.
  echo -e "\n${BOLD}Editing${NORMAL} config file ${BOLD}${CONFIG_FILE}${NORMAL} in place"
  sed -i '' "s,^spring.datasource.url.*,spring.datasource.url=jdbc:mariadb://database:3306/fintech,g" $CONFIG_FILE
}

fCheckCommand docker
fEditConfig

echo -e "\n${BOLD} Starting both database and service...${NORMAL}\n\n"

# This way the containers are removed after finishing
docker compose up && docker compose rm -fsv
