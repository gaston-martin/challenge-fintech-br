#!/bin/bash

# check if stdout is a terminal...
if test -t 1; then
    # see if it supports colors...
    NCOLORS=$(tput colors)
    if test -n "$NCOLORS" && test $NCOLORS -ge 8; then
        BOLD="$(tput bold)"
        UL="$(tput smul)"
        REV="$(tput smso)"
        NORMAL="$(tput sgr0)"
        BLACK="$(tput setaf 0)"
        RED="$(tput setaf 1)"
        GREEN="$(tput setaf 2)"
        YELLOW="$(tput setaf 3)"
        BLUE="$(tput setaf 4)"
        MAGENTA="$(tput setaf 5)"
        CYAN="$(tput setaf 6)"
        WHITE="$(tput setaf 7)"
    fi
fi

fCheckFile(){
  FILE="$1"
  [[ -f "$FILE" ]] || fError "${BOLD}File ${YELLOW}${BOLD}${FILE}${NORMAL}${WHITE} not found"
}

fCheckCommand(){
  which "$1" >/dev/null 2>/dev/null || fError "Command ${YELLOW}${BOLD}${1}${NORMAL}${WHITE} not found"
}

fError(){
  echo -e "\n${RED}ERROR: ${WHITE}$*"
  exit 1
}

fOK(){
  echo -e "${GREEN}${*}${WHITE}"
}
