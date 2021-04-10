#!/bin/bash

source $(dirname ${BASH_SOURCE[0]})/utils.sh

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
  getRoles          get all role
  getRoleByName     get role by name      [roleName]
  addRole           add a role            [roleName]
  updateRoleName    update role name      oldRoleName newRoleName
  addUser           add a user            [username [password(default. password) [phone]]]
  login             login with username   username password
EOF
}

[ $# -eq 0 ] && error "require at least one argument"  && exit 1

GET="curl -v -X GET"
POST="curl -v -X POST "
DELETE="curl -v -X DELETE "
DESTINATION="http://localhost:8099"
NOPROXY="--noproxy localhost,127.0.0.1 "
APPLICATION_JSON=(
    --header 'Content-Type: application/json'
)
BYPASS_AUTHORIZATION=(
    --header 'X-IM-ADMIN: daoyun'
)

addUser() {
    local username=${1:-dummy$RANDOM}
    local password=${2:-password}
    local phone=${3:-$RANDOM}
    local json='{"userName": "'$username'", "password": "'$password'", "phone": "'$phone'" }'
    debug JSON $json

    $POST $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/signup
}

loginByUsername() {
    assert "[ $# -eq 2 ]"
    local username=$1
    local password=$2
    local json='{"userName": "'$username'", "password": "'$password'" }'
    debug JSON $json

    $POST $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/login
}

logout() {
    assert "[ $# -eq 1 ]"
    local token=$1
    local params="refreshToken=$token"

    $DELETE $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        $DESTINATION/apis/auth/login?$params
}

addRole() {
    local rolename=${1:-dr$RANDOM}
    local json='{"roleName": "'$rolename'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/role
}

updateRoleName() {
    assert "[ $# -eq 2 ]" "oldRoleName newRoleName"
    local old=$1
    local new=$2
    local json='{"oldRoleName": "'$old'", "newRoleName": "'$new'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/role/rename
}

getRoleByName() {
    assert "[ $# -eq 1 ]" "require rolename"
    local rolename=$1
    local json='{"roleName": "'$rolename'"}'

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/role/by-name
}

getRoles() {
    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role/all
}

getJwt() {
    assert "[ $# -eq 1 ]" "require refresh token"
    local token=$1
    local params="refreshToken=$token"

    $GET $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        $DESTINATION/apis/auth/jwt?$params
}

COMMAND=$1
shift 1

case $COMMAND in
    "-h"|"--help" )
        usage
        exit 0
        ;;
    "addUser" )
        info "add user"
        addUser $@
        ;;
    "login" )
        info "login"
        loginByUsername $@
        ;;
    "logout" )
        info "logout"
        logout $@
        ;;
    "addRole")
        info "add role"
        addRole $@
        ;;
    "getRoleByName" )
        info "get role by name"
        getRoleByName $@
        ;;
    "getRoles" )
        info "get roles"
        getRoles
        ;;
    "updateRoleName")
        info "update role name"
        updateRoleName $@
        ;;
    "getJwt")
        info "get jwt"
        getJwt $@
        ;;
    *)
        usage
        exit 1
        ;;
esac

