#FROM eclipse-temurin:17-jdk-alpine
FROM bellsoft/liberica-openjdk-alpine-musl:17

ARG USERNAME=""
ARG WORK_DIR="/home/${USERNAME}/app"

#RUN addgroup -S $USERNAME && adduser -S $USERNAME -G $USERNAME
COPY app/build/libs/app.jar $WORK_DIR/app.jar
WORKDIR $WORK_DIR

#RUN chown -R $USERNAME:$USERNAME $WORK_DIR
#RUN chmod 755 $WORK_DIR

#USER $USERNAME

EXPOSE 8080
EXPOSE 5005

#ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/usr/app/app.jar"]
