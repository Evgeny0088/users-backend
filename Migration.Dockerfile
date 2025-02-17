FROM liquibase:4.30-alpine

ARG USERNAME=""
ARG WORK_DIR="/home/${USERNAME}/migration"
ENV JDBC_DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""
ENV LOG_LEVEL=""

WORKDIR $WORK_DIR

COPY app/src/main/resources/migrations ./migrations