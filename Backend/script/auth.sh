#/bin/bash 

source $(dirname ${BASH_SOURCE[0]})/utils.sh

auth() {
    local command=$1
    shift 1

    case $command in
        "login"  ) loginByUsername $@ ;;
        "quick"  ) quickLogin $@ ;;
        "logout" ) logout $@ ;;
        "jwt"    ) getJwt $@ ;;
        "signup" ) signup $@ ;;
        * ) 
            error "unknown subcommand auth '$1'"
            exit 1
            ;;
    esac
}

loginByUsername() {
    assert "[ $# -eq 2 ]"
    local username=$1
    local password=$2
    local json='{"userName": "'$username'", "password": "'$password'" }'
    debug JSON $json

    $POST $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/refresh-token
}

quickLogin() {
    assert "[ $# -eq 3 ]"
    local -A request=(
        ["phone"]=$1
        ["messageCode"]=$2
        ["messageCodeToken"]=$3
    )
    local json=
    jsonStringify request json
    debug JSON $json

    $POST $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/refresh-token/quick
}

logout() {
    assert "[ $# -eq 1 ]"
    local token=$1
    local params="refreshToken=$token"

    $DELETE $NOPROXY \
        "${APPLICATION_JSON[@]}" \
        $DESTINATION/apis/auth/login?$params
}


getJwt() {
    assert "[ $# -eq 1 ]" "require refresh token"
    local token=$1
    local params="refreshToken=$token"

    $GET $NOPROXY \
        $DESTINATION/apis/auth/jwt?$params
}

signup() {
    assert "[ $# -eq 5 ]" "signup username password phone code token"
    local username=$1
    local password=$2
    local phone=$3
    local code=$4
    local token=$5
    local json='{"userName": "'$username'", "password": "'$password'", "phone": "'$phone'", "messageCode": "'$code'", "messageCodeToken": "'$token'"}'
    debug JSON $json

    $POST $NOPROXY "${BYPASS_AUTHORIZATION[@]}" \
        "${APPLICATION_JSON[@]}" \
        --data "$json" \
        $DESTINATION/apis/auth/signup
}

