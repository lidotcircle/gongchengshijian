#!/bin/sh

conff=/etc/nginx/conf.d/default.conf
envsubst '$BACKEND_ADDRESS' < $conff > ${conff}.bb && \
    rm $conff && mv ${conff}.bb $conff

