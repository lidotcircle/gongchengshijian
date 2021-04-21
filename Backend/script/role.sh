#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh


role() {
    assert "[ $# -ge 1 ]"
    local command=$1
    shift 1

    case $command in
        "get"    ) getRoleByName $@ ;;
        "getall" ) getRoles $@ ;;
        "create" ) addRole $@ ;;
        "update" ) updateRoleName $@ ;;
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


