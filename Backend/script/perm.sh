#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

perm_usage() {
    cat <<EOF
    perm subcommand [arguments]

    subcommands:
        get          descriptor
        delete       [-r|--recursive] descriptor
        create       descriptor link [parentDescriptor]
        gettree
        getrole      roleName

        enable       [-r|--recursive] descriptor roleName
        disable      [-r|--recursive] descriptor roleName
EOF
}

perm() {
    assert "[ $# -ge 1 ]"
    local command=$1
    shift 1

    case $command in
        "-h"|"--help" ) perm_usage; exit 0 ;;
        "get"    )  perm_get $@ ;;
        "delete" )  perm_delete $@ ;;
        "create" )  perm_create $@ ;;
        "gettree" ) perm_tree $@ ;;
        "getrole" ) perm_role $@ ;;

        "enable"  ) perm_enable $@ ;;
        "disable" ) perm_disable $@ ;;
        * )
            error "unknow subcommand perm '$1'"
            exit 1
            ;;
    esac
}

perm_get() {
    assert "[ $# -eq 1 ]"
    local -A request=(
        ["descriptor"]="$1"
    )
    local params
    paramsStringify request params

    $GET $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/perm?$params
}

perm_delete() {
    assert "[ $# -ge 1 ]"
    local -A request=(
    )
    if [[ "$1" =~ ^-r|--recursive$ ]]; then
        request["recursive"]="true"
        shift 1
    fi
    request["descriptor"]="$1"
    local params
    paramsStringify request params

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/perm?$params
}

perm_create() {
    assert "[ $# -ge 2 ]"
    local -A request=(
        ["descriptor"]="$1"
        ["link"]="$2"
    )
    if [ $# -ge 3 ]; then
        request["parentDescriptor"]="$3"
    fi
    local json
    jsonStringify request json
    debug JSON "$json"

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/perm?$params
}

perm_tree() {
    $GET $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/perm/tree?$params
}

perm_role() {
    assert "[ $# -eq 1 ]"
    local -A request=(
        ["roleName"]="$1"
    )
    local params
    paramsStringify request params

    $GET $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role/perm-list?$params
}

perm_enable() {
    assert "[ $# -ge 2 ]"
    local -A request=(
    )
    if [[ "$1" =~ ^-r|--recursive$ ]]; then
        request["recursive"]="true"
        shift 1
    fi
    request["descriptor"]="$1"
    request["roleName"]="$2"
    local params
    paramsStringify request params

    $POST $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role/perm?$params
}

perm_disable() {
    assert "[ $# -ge 2 ]"
    local -A request=(
    )
    if [[ "$1" =~ ^-r|--recursive$ ]]; then
        request["recursive"]="true"
        shift 1
    fi
    request["descriptor"]="$1"
    request["roleName"]="$2"
    local params
    paramsStringify request params

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/role/perm?$params
}

