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
  getRoleByName     get role by name           [roleName]
  addRole           add a role                 [roleName]
  updateRoleName    update role name           oldRoleName newRoleName
  addUser           add a user                 [username [password(default. password) [phone]]]
  login             login with username        username password
  userSet           update attribute           attribute value
  userSetP          update priv attribute      password attribute value
  getUserinfo       get user info              [jwtToken]
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
        $DESTINATION/apis/auth/jwt?$params
}

userSet() {
    assert "[ $# -eq 2 ]" "require attributeName value"
    local attribute=$1
    local value=$2
    local json='{"'$attribute'": '$value'}'
    debug JSON $json

    $PUT $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/user
}

userSetP() {
    assert "[ $# -eq 3 ]" "require password attributeName value"
    local password=$1
    local attribute=$2
    local value=$3
    local json='{"requiredPassword": "'$password'", "'$attribute'": '$value'}'
    debug JSON $json

    $PUT $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/user/privileged
}

getUserinfo() {
    assert "[ $# -le 1 ]" "require one or none argument"
    if [ $# -eq 1 ]; then
        $GET $NOPROXY --header "Authorization: $1" \
            $DESTINATION/apis/user
    else
        $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
            $DESTINATION/apis/user
    fi
}

sysparam_get() {
    assert "[ $# -eq 1 ]" "sysparam get <key>"
    local json='{"key": "'$1'"}'
    debug JSON $json

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
}
sysparam_put() {
    assert "[ $# -eq 2 ]" "sysparam put <key> <value>"
    local json='{"key": "'$1'", "value": "'$2'"}'
    debug JSON $json

    $PUT $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
}
sysparam_post() {
    assert "[ $# -eq 2 ]" "sysparam post <key> <value>"
    local json='{"key": "'$1'", "value": "'$2'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
}
sysparam_delete() {
    assert "[ $# -eq 1 ]" "sysparam delete <key>"
    local json='{"key": "'$1'"}'
    debug JSON $json

    $DELETE $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
}

sysparam_getall() {
    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/sysparam/all-key
}
sysparam_getpage() {
    assert "[ $# -eq 2 ]" "sysparam getpage <pageno> <size>"
    local json='{"pageno": '$1', "size": '$2'}'
    debug JSON $json

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam/page
}

sysparam() {
    assert "[ $# -ge 1 ]" "sysparam [get | put | post | delete]"
    local command=$1
    shift 1
    case $command in
        "get" ) sysparam_get $@ ;;
        "put" ) sysparam_put $@ ;;
        "post" ) sysparam_post $@ ;;
        "delete" ) sysparam_delete $@ ;;
        "getall" ) sysparam_getall $@ ;;
        "getpage" ) sysparam_getpage $@ ;;
    esac
}

# Message #{
message() {
    assert "[ $# -ge 1 ]" "message [get]"
    local command=$1
    shift 1
    case $command in
        "get" )
            message_get $@
            ;;
    esac
}

message_get() {
    assert "[ $# -ge 3 ]" "message get phone captcha type"
    local phone=$1
    local captcha=$2
    local type=$3
    local json='{"phone": "'$phone'", "captcha": "'$captcha'", "type": "'$type'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/message
}

#}

# Signup #{
signup() {
    assert "[ $# -eq 5 ]" "signup username password phone code token"
    local username=$1
    local password=$2
    local phone=$3
    local code=$4
    local token=$5
    local json='{"userName": "'$username'", "password": "'$password'", "phone": "'$phone'", "messageCode": "'$code'", "messageCodeToken": "'$token'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/signup
}
#}

COMMAND=$1
shift 1

# COMMANDS #{
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
    "getJwt" )
        info "get jwt"
        getJwt $@
        ;;
    "userSet" )
        info "set user attribute"
        userSet $@
        ;;
    "userSetP" )
        info "set user privileged attribute"
        userSetP $@
        ;;
    "getUserinfo" )
        info "get user info"
        getUserinfo $@
        ;;
    "sysparam" )
        info "sysparam $1"
        sysparam $@
        ;;
    "message" )
        info "message ops"
        message $@
        ;;
    "signup" )
        info "signup $1"
        signup $@
        ;;
    *)
        usage
        exit 1
        ;;
esac
#}

