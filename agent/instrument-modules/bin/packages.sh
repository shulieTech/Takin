#!/bin/bash

CURRENT_PATH="$(
  cd $(dirname $0)
  pwd
)"
PROJECT_PATH=$CURRENT_PATH/..

TARGET_DIR=$PROJECT_PATH/target

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err() {
  [[ ! -z "${2}" ]] && echo "${2}" 1>&2
  exit ${1}
}

mvn clean install -Dmaven.test.skip=true -f ../pom.xml ||
  exit_on_err 1 "package instrument modules failed."

cp ../biz-classloader-inject/biz-classloader-inject.properties ../target/biz-classloader-jars/biz-classloader-inject.properties