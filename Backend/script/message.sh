#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh


message() {
    assert "[ $# -ge 1 ]" "message [get]"
    local command=$1
    shift 1
    case $command in
        "get" ) message_get $@ ;;
        * )
            error "unknow subcommand message '$1'"
            exit 1
            ;;
    esac
}

message_get() {
    assert "[ $# -ge 2 ]" "message get phone type [captcha]"
    local phone=$1
    local captcha=$2
    local type=$3
    local -A request=(
        ["phone"]=$1
        ["type"]=$2
    )
    [ $# -eq 3 ] && ["captcha"]=$3
    local json=
    jsonStringify request json
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/message
}

