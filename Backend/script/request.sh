#!/bin/bash

source $(dirname ${BASH_SOURCE[0]})/utils.sh
source $(dirname ${BASH_SOURCE[0]})/auth.sh
source $(dirname ${BASH_SOURCE[0]})/course.sh
source $(dirname ${BASH_SOURCE[0]})/message.sh
source $(dirname ${BASH_SOURCE[0]})/role.sh
source $(dirname ${BASH_SOURCE[0]})/sysparam.sh
source $(dirname ${BASH_SOURCE[0]})/user.sh

START_TIME_NS=$(date +%s%N)
echoExecutionTime() {
    NOW=$(date +%s%N)
    TIME_MS=$[($NOW - $START_TIME_NS) / 10**6]

    printf "\n\n"
    info "exit after ${TIME_MS}ms"
}
trap echoExecutionTime EXIT

[ -z "$(which curl)" ] && error "cURL: require curl" && exit 1

usage() {
    cat <<EOF
Usage: $0 <command> <comand-arguments>

  -h, --help    print this help

commands:
  role              [ get | getall | create | update ]
  message           [ get ]
  auth              [ login | logout | jwt | signup ]
  user              [ get | set | setP | create ]
  sysparam          [ get | put | post | delete | getall | getpage ]
  course            [ create | update | get | delete ]
EOF
}

[ $# -eq 0 ] && error "require at least one argument"  && exit 1

GET="curl -v -X GET"
POST="curl -v -X POST "
PUT="curl -v -X PUT "
DELETE="curl -v -X DELETE "
DESTINATION="http://localhost:8099"
NOPROXY="--noproxy localhost,127.0.0.1 "
APPLICATION_JSON=(
    --header 'Content-Type: application/json'
)
BYPASS_AUTHORIZATION=(
    --header 'X-IM-ADMIN: daoyun'
)

COMMAND=$1
shift 1

case $COMMAND in
    "-h"|"--help" )
        usage
        exit 0
        ;;
    "role"     ) role     $@ ;;
    "message"  ) message  $@ ;;
    "auth"     ) auth     $@ ;;
    "user"     ) user     $@ ;;
    "sysparam" ) sysparam $@ ;;
    "course"   ) course   $@ ;;
    *)
        usage
        exit 1
        ;;
esac

