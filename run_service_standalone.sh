#!/bin/bash


source commons.sh

trap fStopDatabase Exit


fStopDatabase(){
  echo -e "\n${BOLD}Exit detected. Stopping database...${NORMAL}"
  ./stop_database.sh
  docker compose rm -fsv || true
}

fCheckJavaVersion() {
  java --version | egrep -q " 21"
  if [ $? -ne 0 ]; then
    which java
    java --version
    fError "${BOLD}Java 21${NORMAL} is required to run this application"
  else
    fOK "\nJava version is right\n"
    java -version
  fi
}

fCheckJavaCompilerFound(){
  which javac
  if [ $? -ne 0 ]; then
    fError "${BOLD}Java compiler 'javac' not found in path"
  else
    fOK "\nJava compiler found"
  fi
}

fEditConfig(){
  CONFIG_FILE="src/main/resources/application.properties"
  [ -f "$CONFIG_FILE" ] || fError "Unable to find config file $CONFIGFILE"

  # This is needed because localhost is local to container when run in docker
  # But there is no standard and portable way of pointing to host's network from within container
  # So when running standalone, localhost is used along with mapped port
  echo -e "\n${BOLD}Editing${NORMAL} config file ${BOLD}${CONFIG_FILE}${NORMAL} in place"
  sed -i '' "s,^spring.datasource.url.*,spring.datasource.url=jdbc:mariadb://localhost:33067/fintech,g" $CONFIG_FILE
}

fLoopSeconds(){
  echo -e "\n${BOLD}Waiting 10 seconds to let the database getting ready${NORMAL}"
  for J in {1..10}
  do
    sleep 1
    echo -e ".\c"
  done
  echo -e "\n"
}

fCheckFile build.gradle
fCheckFile gradlew
fCheckCommand docker
fCheckCommand java
fCheckCommand javac
echo -e "\n${BOLD}Checking if current Java Runtime points to a JAVA 21 version${NORMAL}"
fCheckJavaVersion
fCheckJavaCompilerFound
fEditConfig

# Start container for the database
./start_database.sh

fLoopSeconds

echo -e "\n${BOLD}Starting service...${NORMAL}\n\n"

./gradlew bootRun


