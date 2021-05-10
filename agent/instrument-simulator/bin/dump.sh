#!/bin/sh
#jmap -dump:format=b,file=/Users/angju/IdeaProjects/pradar-javaagent-temp/pradar-javaagent/dist/target/pradar-javaagent/$1.hprof  $2
if [ "true" -eq "$3" ]; then
    jmap -dump:live,format=b,file=$1 $2
else
    jmap -dump:format=b,file=$1 $2
fi
#sleep 5s
touch $1.finished
#nohup java -Xms32m -Xmx32m -jar $4/arthas-boot.jar $1  --http-port $2 --telnet-port $3 &
