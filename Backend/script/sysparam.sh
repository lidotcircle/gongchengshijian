#!/bin/bash

source $(dirname ${BASH_SOURCE[0]})/utils.sh

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
        * )
            error "unknown subcommand sysparam '$1'"
            exit 1
            ;;
    esac
}

#{ get
sysparam_get()
{
    assert "[ $# -eq 1 ]" "sysparam get <key>"
    local json='{"key": "'$1'"}'
    debug JSON $json

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
} #}
#{ put
sysparam_put() {
    assert "[ $# -eq 2 ]" "sysparam put <key> <value>"
    local json='{"key": "'$1'", "value": "'$2'"}'
    debug JSON $json

    $PUT $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
} #}
#{ post
sysparam_post() {
    assert "[ $# -eq 2 ]" "sysparam post <key> <value>"
    local json='{"key": "'$1'", "value": "'$2'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
} #}
#{ delete
sysparam_delete() {
    assert "[ $# -eq 1 ]" "sysparam delete <key>"
    local json='{"key": "'$1'"}'
    debug JSON $json

    $DELETE $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam
} #}

#{ getall
sysparam_getall() {
    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        $DESTINATION/apis/sysparam/all-key
} #}
#{ getpage
sysparam_getpage() {
    assert "[ $# -eq 2 ]" "sysparam getpage <pageno> <size>"
    local json='{"pageno": '$1', "size": '$2'}'
    debug JSON $json

    $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/sysparam/page
} #}

