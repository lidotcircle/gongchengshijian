#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

role_usage() {
    cat <<EOF
    role subcommand <arguments>

    subcommands:
        get        roleName
        getall
        create     roleName
        perm       roleName
EOF
}

role() {
    assert "[ $# -ge 1 ]"
    local command=$1
    shift 1

    case $command in
        "-h" | "--help" ) role_usage; exit 0 ;;
        "get"    ) getRoleByName $@ ;;
        "getall" ) getRoles $@ ;;
        "create" ) addRole $@ ;;
        "perm"   ) getRolePerms $@ ;;
        * )
            error "unknow subcommand role '$1'"
            exit 1
            ;;
    esac
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

getRoleByName() {
    assert "[ $# -eq 1 ]" "require rolename"
    local -A request=(
        ["roleName"]=$1
    )
    local params
    paramsStringify  request params

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role?$params
}

getRoles() {
    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role/list
}

getRolePerms() {
    assert "[ $# -eq 1 ]" "require rolename"
    local -A request=(
        ["roleName"]=$1
    )
    local params
    paramsStringify  request params

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role/perm/list?$params
}

