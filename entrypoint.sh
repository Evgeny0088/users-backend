#!/bin/sh
#
keytool -import -alias keycloak_crt \
-file ./cert/tls.crt \
-keystore ${JAVA_HOME}/lib/security/cacerts  \
-storepass changeit -noprompt && \
java \
-Dspring-boot.run.jvmArguments=${JAVA_OPTS} \
-Dspring.profiles.active=${SPRING_PROFILE} \
-Duser.timezone=Europe/Moscow \
-jar ./app.jar

