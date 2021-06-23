#!/bin/bash

if [ -n "${API_ADDRESS}" ]; then
    if [[ $API_ADDRESS =~ ^https\?://[^/]*$ ]]; then
        echo "incorrect http address"
    fi

    unescaped_addr=$(echo $API_ADDRESS | sed 's/\//\\\//g')
    sed -i "s/API_ADDRESS_BUILD_ENV/$unescaped_addr/g" ./src/app/shared/utils.ts
fi

