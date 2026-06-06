#!/bin/sh
set -eu

PORT_VALUE="${PORT:-8080}"
SERVER_XML="/usr/local/tomcat/conf/server.xml"

sed -i "s/port=\"8080\" protocol=\"HTTP\\/1.1\"/port=\"${PORT_VALUE}\" protocol=\"HTTP\\/1.1\"/g" "${SERVER_XML}"

exec catalina.sh run
