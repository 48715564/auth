#!/usr/bin/env bash
set -e
set -x

COLLECTOR_IP=${COLLECTOR_IP:-127.0.0.1}
tcp_port=${tcp_port:-9994}
stat_port=${stat_port:-9995}
span_port=${span_port:-9996}
hostname=$(cat /etc/hostname)
cp -f /assets/pinpoint.config /assets/pinpoint-agent/pinpoint.config
sed -i "s/profiler.collector.ip=127.0.0.1/profiler.collector.ip=${COLLECTOR_IP}/g" /assets/pinpoint-agent/pinpoint.config
sed -i "s/profiler.collector.tcp.port=9994/profiler.collector.tcp.port=${tcp_port}/g" /assets/pinpoint-agent/pinpoint.config
sed -i "s/profiler.collector.stat.port=9995/profiler.collector.stat.port=${stat_port}/g" /assets/pinpoint-agent/pinpoint.config
sed -i "s/profiler.collector.span.port=9996/profiler.collector.span.port=${span_port}/g" /assets/pinpoint-agent/pinpoint.config
java -javaagent:/assets/pinpoint-agent/pinpoint-bootstrap-1.6.2.jar -Dpinpoint.agentId=${hostname} -Dpinpoint.applicationName=auth -jar /app.jar --spring.profiles.active=dev