#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

course() {
    assert "[ $# -ge 1 ]" "bad command $@"
    local command=$1
    shift 1

    case $command in
        "get" ) course_get $@ ;;
        "delete" ) course_delete $@ ;;
        "create" ) course_create $@ ;;
        "update" ) course_update $@ ;;
        * )
            error "unknow subcommand course '$1'"
            exit 1
            ;;
    esac
}

course_get() {
    local -A request=(
        ["courseExId"]="$1"
    )
    local params
    paramsStringify request params

    $GET $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course?$params
}

course_delete() {
    local -A request=(
        ["courseExId"]="$1"
    )
    local params
    paramsStringify request params

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course?$params
}

course_create() {
    assert "[ $# -eq 3 ]" "require 3 argument"

    local -A request=(
        ["courseName"]="$1"
        ["teacherName"]="$2"
        ["briefDescription"]="$3"
    )
    local json
    jsonStringify request json
    debug JSON $json

    $POST $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/course
}

course_update() {
    assert "[ $# -eq 3 ]" "require 3 argument"

    local -A request=(
        ["courseExId"]="$1"
        ["$2"]="$3"
    )
    local json
    jsonStringify request json
    debug JSON $json

    $PUT $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/course
}

