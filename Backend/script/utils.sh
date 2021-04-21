
source $(dirname ${BASH_SOURCE[0]})/utils/color.sh
source $(dirname ${BASH_SOURCE[0]})/utils/logger.sh
source $(dirname ${BASH_SOURCE[0]})/utils/assert.sh

#{ JSON stringify
jsonStringify()
{
    local -n jsonRef=$1
    local -n outputRef=$2
    local hasKey=
    outputRef="{"

    for key in "${!jsonRef[@]}"; do
        hasKey="yes"
        local value=${jsonRef[$key]}

        if [ -z "$value" ]; then
            value="null"
        fi

        if [[ ! "$value" =~ ^[0-9]+(\.[0-9]+)?|true|false|null$ ]]; then
            value=\"$value\"
        fi
        
        outputRef=$outputRef"\"$key\": "$value","
    done

    if [ -n "$hasKey" ]; then
        outputRef=$(echo $outputRef | rev | cut -c2- | rev)
    fi

    outputRef=$outputRef"}"
} #}

#{ params stringify
paramsStringify()
{
    local -n objRef=$1
    local -n outputRef=$2
    local hasKey=
    outputRef=""

    for key in "${!objRef[@]}"; do
        hasKey="yes"
        local value=${objRef[$key]}
        outputRef=${outputRef}${key}"="$value"&"
    done

    if [ -n "$hasKey" ]; then
        outputRef=$(echo $outputRef | rev | cut -c2- | rev)
    fi
} #}

