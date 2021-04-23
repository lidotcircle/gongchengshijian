#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

courseTask_usage() {
    cat <<EOF
    courseTask [options] subcommand [arguments]

    options:
        -s | --super     as administrator

    subcommands:
        get              taskId
        delete           taskId
        create           courseExId title content [committable [deadline]]
        update           taskId prop newvalue
        list             courseExId
EOF
}

super=
if [ "$1" == '-s' ] || [ "$1" == '--super' ]; then
    super='/super'
    shift 1
fi

courseTask() {
    assert "[ $# -ge 1 ]" "bad command $@"
    local command=$1
    shift 1

    case $command in
        "-h"|"--help" ) courseTask_usage; exit 0 ;;
        "get" )    courseTask_get $@ ;;
        "delete" ) courseTask_delete $@ ;;
        "create" ) courseTask_create $@ ;;
        "update" ) courseTask_update $@ ;;
        "list" )   courseTask_list $@ ;;
        * )
            error "unknow subcommand courseTask '$1'"
            exit 1
            ;;
    esac
}

courseTask_get() {
    assert "[ $# -eq 1 ]"
    local -A request=(
        ["taskId"]="$1"
    )
    local params
    paramsStringify request params

    $GET $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course/task$super?$params
}

courseTask_delete() {
    assert "[ $# -eq 1 ]"
    local -A request=(
        ["taskId"]="$1"
    )
    local params
    paramsStringify request params

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course/task$super?$params
}

courseTask_create() {
    assert "[ $# -ge 3 ]" "require at least 3 argument"

    local -A request=(
        ["courseExId"]="$1"
        ["taskTitle"]="$2"
        ["content"]="$3"
    )
    [ $# -ge 4 ] && request["committable"]="$4"
    [ $# -ge 5 ] && request["deadline"]="$5"
    local json
    jsonStringify request json
    debug JSON $json

    $POST $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/course/task$super
}

courseTask_update() {
    assert "[ $# -eq 3 ]" "require 3 argument"

    local -A request=(
        ["taskId"]="\"$1\""
        ["$2"]="$3"
    )
    local json
    jsonStringify request json
    debug JSON $json

    $PUT $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/course/task$super
}

