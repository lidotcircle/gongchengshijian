#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

user() {
    local command=$1
    shift 1

    case $command in
        "get" ) user_get $@ ;;
        "set" ) user_set $@ ;;
        "setP" ) user_setP $@ ;;
        "create" ) user_create $@ ;;
        * ) 
            error "unknown subcommand course '$1'"
            exit 1
            ;;
    esac
}


user_create() {
    local username=${1:-dummy$RANDOM}
    local password=${2:-password}
    local phone=${3:-$RANDOM}
    local json='{"userName": "'$username'", "password": "'$password'", "phone": "'$phone'" }'
    debug JSON $json

    $POST $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/signup
}

user_set() {
    assert "[ $# -eq 2 ]" "require attributeName value"
    local attribute=$1
    local value=$2
    local json='{"'$attribute'": '$value'}'
    debug JSON $json

    $PUT $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/user
}

user_setP() {
    assert "[ $# -eq 3 ]" "require password attributeName value"
    local password=$1
    local attribute=$2
    local value=$3
    local json='{"requiredPassword": "'$password'", "'$attribute'": '$value'}'
    debug JSON $json

    $PUT $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/user/privileged
}

user_get() {
    assert "[ $# -le 1 ]" "require one or none argument"
    if [ $# -eq 1 ]; then
        $GET $NOPROXY --header "Authorization: $1" \
            $DESTINATION/apis/user
    else
        $GET $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
            $DESTINATION/apis/user
    fi
}

