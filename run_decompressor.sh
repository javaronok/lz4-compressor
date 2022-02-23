#!/bin/bash

JVM_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
APP_HOME=$(dirname $0)

CLASSPATH=$(echo "$APP_HOME"/build/libs/*.jar | tr ' ' ':')

RUN_CMD="java ${JVM_OPTS} -cp ${CLASSPATH}"

${RUN_CMD} com.gda.system.LZ4DataDecompressor -file data/compressed.out