#!/bin/sh

java -jar -D@appId=alfi-example -D@environment=local $* -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 build/libs/*.jar