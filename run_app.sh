#!/bin/bash
APP_IMAGE='docker-image/users-backend:1.0.0'
MIGRATION_IMAGE='docker-image/users-backend-migration:1.0.0'
CHECK_MARK='\xE2\x9C\x94'
UPSET="\U0001f62D";
JET="\U0001f680";
FIRED="\U0001f525";

inspect_and_build_image() {
    if docker image inspect $1 >/dev/null 2>&1; then
      echo -e "Found old image, removing it ...${FIRED}\n"
      docker rmi -f $1
      echo -e "\n"
    fi

    if ! docker build --build-arg USERNAME=$2 --tag $1 $3; then
      echo -e "fails to create docker image... ${UPSET}" && exit $?
    else
      echo -e "image is created!... ${CHECK_MARK}\n"
    fi
}

build_users_backend_image() {
  echo -e "tests are started ... ${JET}"

  if ! ./gradlew clean build; then
    if ! ./gradlew clean build -x test --parallel --debug --no-daemon --info; then
     echo -e "tests are failed... ${UPSET}" && exit $?
    fi
  else
     echo -e "tests are passed, jar file is created!... ${CHECK_MARK}\n"
     sleep 1
  fi

  inspect_and_build_image ${APP_IMAGE} ${USERNAME} "."
}

build_liquibase_image() {
    inspect_and_build_image ${MIGRATION_IMAGE} ${USERNAME} "-f Migration.Dockerfile ."
}

# build images
#build_liquibase_image
#build_users_backend_image

# install migration helm chart
#helm upgrade --install \
#    --atomic \
#    --timeout 30m \
#    --wait \
#    --wait-for-jobs \
#    --namespace backend-ns \
#    --set global.applicationNs=application-ns \
#    --set global.usersDbConfigMap=users-db-config-map \
#    --set global.usersDbSecret=users-db-secret \
#    helm-migration-job \
#    ./helm/users-chart/charts/migration-job

# install users-chart
helm upgrade --install \
    --atomic \
    --timeout 20m \
    --wait \
    --wait-for-jobs \
    --namespace backend-ns \
    --set keycloak.realm=$REALM \
    --set keycloak.clientId=$CLIENT_ID \
    --set keycloak.clientSecret=$CLIENT_SECRET \
    --set email.host=$EMAIL_HOST \
    --set email.port=$EMAIL_PORT \
    --set email.username=$EMAIL \
    --set email.password=$EMAIL_PASS \
    --set email.emailSendTo=$EMAIL_SEND_TO \
    helm-users-backend \
    ./helm/users-backend

### test commands ###

# run container in shell and check content
    #eval $(minikube docker-env)
    #docker build --build-arg USERNAME=${USERNAME} --tag docker-image/users-backend:1.0.0 .
    # keytool -list -keystore $JAVA_HOME/lib/security/cacerts -alias backend_cert -storepass changeit -noprompt
    # docker run --rm -it --name users-backend docker-image/users-backend:1.0.0 sh
    # try to find file : find / -type f -name "tls.crt"
    # check user: echo "$USER" or whoami

# dry-run helm
#helm template --dry-run --debug \
#        --set keycloak.realm=$REALM \
#        --set keycloak.clientId=$CLIENT_ID \
#        --set keycloak.clientSecret=$CLIENT_SECRET \
#        ./helm/users-backend
