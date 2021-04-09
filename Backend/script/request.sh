#!/bin/bash

source $(dirname ${BASH_SOURCE[0]})/utils.sh

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
  addUser           add a user            [username [phone]]
EOF
}

[ $# -eq 0 ] && error "require at least one argument"  && exit 1

GET="curl -v -X GET"
POST="curl -v -X POST "
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
    local phone=${2:-$RANDOM}
    local json='{"userName": "'$username'", "password": "password", "phone": "'$phone'" }'
    debug JSON $json

    $POST $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/signup
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
    *)
        usage
        exit 1
        ;;
esac

