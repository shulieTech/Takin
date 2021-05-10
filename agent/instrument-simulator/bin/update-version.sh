#!/bin/bash

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err() {
  [[ ! -z "${2}" ]] && echo "${2}" 1>&2
  exit ${1}
}

# maven package the simulator
if [ $# != 0 ]; then
    mvn versions:set -DnewVersion=$1 -f ../pom.xml ||
      exit_on_err 1 "set version $1 failed."
else
    exit_on_err 1 "please set the version number."
fi

mvn versions:update-parent -f ../pom.xml
mvn versions:update-child-modules -f ../pom.xml
echo "update version to $1 finish."
