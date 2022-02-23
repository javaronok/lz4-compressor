#!/bin/bash

JVM_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
APP_HOME=$(dirname $0)

CLASSPATH=$(echo "$APP_HOME"/build/libs/*.jar | tr ' ' ':')

RUN_CMD="java ${JVM_OPTS} -cp ${CLASSPATH}"

${RUN_CMD} com.gda.system.LZ4DataCompressor \
-lvl 0 \
-data "As Gregor Samsa awoke one morning from uneasy dreams he found himself transformed in his bed into a gigantic insect. He was lying on his hard, as it were armour plated, back, and if he lifted his head a little he could see his big, brown belly divided into stiff, arched segments, on top of which the bed quilt could hardly keep in position and was about to slide off completely. His numerous legs, which were pitifully thin compared to the rest of his bulk, waved helplessly before his eyes. What has happened to me?, he thought. It was no dream..."