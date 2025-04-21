#!/bin/bash


source commons.sh

fCheckCommand docker

echo -e "\n${BOLD}Checking if there is a running database container...${NORMAL}"

for CONTAINER in $(docker ps -a -q --filter="name=database")
do
   echo "${YELLOW}Found a running database container (${CONTAINER})${WHITE}"
   docker ps --filter="id=$CONTAINER"
   echo -e "\n${BOLD}Stopping... ${NORMAL}\n"
   docker stop ${CONTAINER}
done
