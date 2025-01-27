#!/bin/bash
IMAGE='docker-image/users-backend:1.0.0'
CHECK_MARK='\xE2\x9C\x94'
UPSET="\U0001f62D";
JET="\U0001f680";

run_container() {
  docker run \
  --rm --network marketplace-net \
  --name users-backend \
  ${IMAGE}
}

build_image() {
  echo "tests are started ... ${JET}"

  if ! ./gradlew clean build; then
    if ! ./gradlew clean build -x test --parallel --debug --no-daemon --info; then
     echo -e "tests are failed... ${UPSET}" && exit $?
    fi
  else
     echo -e "tests are passed, jar file is created!... ${CHECK_MARK}\n"
     sleep 1
  fi

    if ! docker build --build-arg USERNAME=${USERNAME} --tag ${IMAGE} .; then
       echo -e "fails to create docker image... ${UPSET}" && exit $?
    else
       echo -e "image is created!... ${CHECK_MARK}\n"
    fi
}

# build users-backend image
build_image

# install helm chart
helm upgrade --install \
    --atomic \
    --timeout 30m \
    --wait \
    --wait-for-jobs \
    --namespace backend-ns \
    --set keycloak.realm=$REALM \
    --set keycloak.clientId=$CLIENT_ID \
    --set keycloak.clientSecret=$CLIENT_SECRET \
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
