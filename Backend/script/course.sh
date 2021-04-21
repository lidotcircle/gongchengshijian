#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

course_usage() {
    cat <<EOF
    course subcommand [arguments]

    subcommands:
        get          courseExId
        delete       courseExId
        create       courseName teacherName briefDescription
        update       courseExId property newvalue
        getpage      pageno size sortDir sortKey wildcard
        getpage_t    pageno size sortDir sortKey wildcard
        getpage_s    pageno size sortDir sortKey wildcard
EOF
}

course() {
    assert "[ $# -ge 1 ]" "bad command $@"
    local command=$1
    shift 1

    case $command in
        "-h"|"--help" ) course_usage; exit 0 ;;
        "get" ) course_get $@ ;;
        "delete" ) course_delete $@ ;;
        "create" ) course_create $@ ;;
        "update" ) course_update $@ ;;
        "getpage" ) course_getpage $@ ;;
        "getpage_s" ) course_getpage_student $@ ;;
        "getpage_t" ) course_getpage_teacher $@ ;;
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
        ["courseExId"]="\"$1\""
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

course_getpage_target() {
    local target=$1
    shift 1
    if [ -n "$target" ]; then
        target="/${target}"
    fi
    assert "[ $# -le 5 ]" "at most 5 argument"

    local -A request=(
        ["pageno"]="${1:-1}"
        ["size"]="${2:-10}"
        ["sortDir"]="${3:-desc}"
        ["sortKey"]="${4:-courseName}"
    )
    if [ $# -eq 5 ]; then
        request['searchWildcard']=$5
    fi
    local params
    paramsStringify request params

    $GET $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course/page$target?$params
}

course_getpage() {
    course_getpage_target "" $@
}

course_getpage_teacher() {
    course_getpage_target "teacher" $@
}

course_getpage_student() {
    course_getpage_target "student" $@
}

