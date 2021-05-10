#!/bin/bash

# simulator's target dir
SIMULATOR_TARGET_DIR=../target/simulator

# exit shell with err_code
# $1 : err_code
# $2 : err_msg
exit_on_err() {
  [[ ! -z "${2}" ]] && echo "${2}" 1>&2
  exit ${1}
}



# maven package the simulator
if [ $# != 0 ]; then
    mvn clean package install -P $1 -Dmaven.test.skip=true -f ../pom.xml ||
      exit_on_err 1 "package simulator failed."
else
    mvn clean package install -Dmaven.test.skip=true -f ../pom.xml ||
      exit_on_err 1 "package simulator failed."
fi
# reset the target dir
mkdir -p ${SIMULATOR_TARGET_DIR}/lib
mkdir -p ${SIMULATOR_TARGET_DIR}/bootstrap
mkdir -p ${SIMULATOR_TARGET_DIR}/biz-classloader-jars
mkdir -p ${SIMULATOR_TARGET_DIR}/system
mkdir -p ${SIMULATOR_TARGET_DIR}/config
mkdir -p ${SIMULATOR_TARGET_DIR}/bin
mkdir -p ${SIMULATOR_TARGET_DIR}/provider
mkdir -p ${SIMULATOR_TARGET_DIR}/modules

# copy jar to TARGET_DIR
cp ../instrument-simulator-core/target/instrument-simulator-core-*-jar-with-dependencies.jar ${SIMULATOR_TARGET_DIR}/lib/instrument-simulator-core.jar &&
  cp ../instrument-simulator-agent/target/instrument-simulator-agent-*-jar-with-dependencies.jar ${SIMULATOR_TARGET_DIR}/instrument-simulator-agent.jar &&
  cp ../instrument-simulator-messager/target/instrument-simulator-messager-*-jar-with-dependencies.jar ${SIMULATOR_TARGET_DIR}/bootstrap/instrument-simulator-messager.jar &&
  cp simulator-logback.xml ${SIMULATOR_TARGET_DIR}/config/simulator-logback.xml &&
  cp simulator.properties ${SIMULATOR_TARGET_DIR}/config/simulator.properties &&
  cp dump.sh ${SIMULATOR_TARGET_DIR}/bin/dump.sh

# simulator's version
SIMULATOR_VERSION=$(cat ..//instrument-simulator-core/target/classes/com/shulie/instrument/simulator/version)
echo "${SIMULATOR_VERSION}" >${SIMULATOR_TARGET_DIR}/config/version

# for mgr
cp ../instrument-simulator-management-provider/target/instrument-simulator-management-provider-*-jar-with-dependencies.jar ${SIMULATOR_TARGET_DIR}/provider/instrument-simulator-management-provider.jar

# make it execute able
chmod +x ${SIMULATOR_TARGET_DIR}/*.sh

# zip the simulator.zip
cd ../target/
zip -r simulator-${SIMULATOR_VERSION}-bin.zip simulator/
cd -

# tar the simulator.tar
cd ../target/
tar -zcvf simulator-${SIMULATOR_VERSION}-bin.tar simulator/
cd -

# release stable version
cp ../target/simulator-${SIMULATOR_VERSION}-bin.zip ../target/simulator-stable-bin.zip
cp ../target/simulator-${SIMULATOR_VERSION}-bin.tar ../target/simulator-stable-bin.tar

echo "package simulator-${SIMULATOR_VERSION}-bin.zip finish."
