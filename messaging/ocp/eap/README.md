BUILD IS
--------
    oc new-build --name=eap-messaging  --binary=true --image-stream=openshift/jboss-eap72-openshift:latest
    cd ocp/eap
    oc start-build eap-messaging  --from-dir .
    oc new-app eap-messaging -e MQ_SERVICE_PREFIX_MAPPING=AMQ7 -e AMQ_TCP_SERVICE_HOST=amq-broker7 -e AMQ_TCP_SERVICE_PORT=61616 -e DISABLE_EMBEDDED_JMS_BROKER=true -e AMQ7_QUEUES=RH.JBOSS.QUEUE -e AMQ7_USERNAME=amquser -e AMQ7_PASSWORD=password \
                             -e MYSQL_JNDI=java:jboss/datasources/integrationDB -e MYSQL_USERNAME=mysql -e MYSQL_PASSWORD=mysql -e MYSQL_DATABASE=integrationdb -e DB_SERVICE_PREFIX_MAPPING=integrationdb-mysql=MYSQL -e INTEGRATIONDB_MYSQL_SERVICE_HOST=mysql -e INTEGRATIONDB_MYSQL_SERVICE_PORT=3306 
    oc expose svc/eap-messaging

Variabili d'ambiente
--------------------
Tali variabili permettono di usare l'immagine base di EAP   

    - name: AMQ7_PASSWORD
      value: password
    - name: AMQ7_QUEUES
      value: RH.JBOSS.QUEUE
    - name: AMQ7_USERNAME
      value: amquser
    - name: AMQ_TCP_SERVICE_HOST
      value: amq-broker7
    - name: AMQ_TCP_SERVICE_PORT
      value: "61616"
    - name: DISABLE_EMBEDDED_JMS_BROKER
      value: "true"
    - name: MQ_SERVICE_PREFIX_MAPPING
      value: AMQ7
    - name: MYSQL_JNDI
      value: java:jboss/datasources/integrationDB
    - name: MYSQL_USERNAME
      value: mysql
    - name: MYSQL_PASSWORD
      value: mysql
    - name: MYSQL_DATABASE
      value: integrationdb
    - name: DB_SERVICE_PREFIX_MAPPING
      value: integrationdb-mysql=MYSQL
    - name: SCRIPT_DEBUG
      value: "true"
    - name: INTEGRATIONDB_MYSQL_SERVICE_HOST
      value: mysql
    - name: INTEGRATIONDB_MYSQL_SERVICE_PORT
      value: "3306"