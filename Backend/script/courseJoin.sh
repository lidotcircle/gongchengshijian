#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

courseJoin_usage() {
    cat <<EOF
    course subcommand [arguments]

    subcommands:
        join         courseExId
        exit         courseExId
        invite       courseExId userName
        delete       courseExId userName
EOF
}

courseJoin() {
    assert "[ $# -ge 2 ]" "bad command $@"
    local command=$1
    shift 1

    case $command in
        "-h"|"--help" ) courseJoin_usage; exit 0 ;;
        "join" ) courseJoin_join $@ ;;
        "exit" ) courseJoin_exit $@ ;;
        "invite" ) courseJoin_invite $@ ;;
        "delete" ) courseJoin_delete $@ ;;
        * )
            error "unknow subcommand course '$1'"
            exit 1
            ;;
    esac
}

courseJoin_join() {
    assert "[ $# -eq 1 ]" "require 1 argument"

    local -A request=(
        ["courseExId"]="$1"
    )
    local json
    jsonStringify request json
    debug JSON $json

    $POST $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/course/student/me
}

courseJoin_exit() {
    assert "[ $# -eq 1 ]" "require 1 argument"

    local -A request=(
        ["courseExId"]="$1"
    )
    local params
    paramsStringify request params

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course/student/me?$params
}

courseJoin_invite() {
    assert "[ $# -eq 2 ]" "require 2 argument"

    local -A request=(
        ["courseExId"]="$1"
        ["studentName"]="$2"
    )
    local json
    jsonStringify request json
    debug JSON $json

    $POST $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/course/student
}

courseJoin_delete() {
    assert "[ $# -eq 2 ]"

    local -A request=(
        ["courseExId"]="$1"
        ["studentName"]=$2
    )
    local params
    paramsStringify request params

    $DELETE $NOPROXY \
        "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/course/student?$params
}


