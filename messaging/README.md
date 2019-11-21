Messaging: Application Using an MDB (Message-Driven Bean)
============================================================
Author: mauiroma
Technologies: JMS, EJB, MDB, HIBERNATE, JSON
Summary: The `messaging` uses *JMS* and *EJB Message-Driven Bean* (MDB) .

What is it?
-----------

The `messaging` demonstrates the use of *JMS* and *EJB Message-Driven Bean* in Red Hat JBoss Enterprise Application Platform.


Push Image into OCP Docker Registry
-----------------------------------
    docker pull registry.redhat.io/amq7/amq-broker
    docker tag 93edd5dad839 docker-registry-default.apps.nodisk.space/workshop-middleware/amq-broker7:latest
    docker push docker-registry-default.apps.nodisk.space/workshop-middleware/amq-broker7:latest


Build and Deploy into OCP
-------------------------
    mvn clean package
    
    cp target/messaging.war ocp/eap/deployments

    oc new-build --name=eap-messaging  --binary=true --image-stream=openshift/jboss-eap72-openshift:latest
    cd ocp/eap
    oc start-build eap-messaging  --from-dir .
    oc new-app eap-messaging -e MQ_SERVICE_PREFIX_MAPPING=AMQ7 -e AMQ_TCP_SERVICE_HOST=amq-broker7 -e AMQ_TCP_SERVICE_PORT=61616 -e DISABLE_EMBEDDED_JMS_BROKER=true -e AMQ7_QUEUES=RH.JBOSS.QUEUE -e AMQ7_USERNAME=amquser -e AMQ7_PASSWORD=password \
                             -e MYSQL_JNDI=java:jboss/datasources/integrationDB -e MYSQL_USERNAME=mysql -e MYSQL_PASSWORD=mysql -e MYSQL_DATABASE=integrationdb -e DB_SERVICE_PREFIX_MAPPING=integrationdb-mysql=MYSQL -e INTEGRATIONDB_MYSQL_SERVICE_HOST=mysql -e INTEGRATIONDB_MYSQL_SERVICE_PORT=3306 
    oc expose svc/eap-messaging



    oc new-app eap-messaging-git openshift/jboss-eap72-openshift:latest~https://gitlab.consulting.redhat.com/consulting-italy/esercito-workshop.git --context-dir=messaging -e MQ_SERVICE_PREFIX_MAPPING=AMQ7 -e AMQ_TCP_SERVICE_HOST=amq-broker7 -e AMQ_TCP_SERVICE_PORT=61616 -e DISABLE_EMBEDDED_JMS_BROKER=true -e AMQ7_QUEUES=RH.JBOSS.QUEUE -e AMQ7_USERNAME=amquser -e AMQ7_PASSWORD=password \
                                                                    -e MYSQL_JNDI=java:jboss/datasources/integrationDB -e MYSQL_USERNAME=mysql -e MYSQL_PASSWORD=mysql -e MYSQL_DATABASE=integrationdb -e DB_SERVICE_PREFIX_MAPPING=integrationdb-mysql=MYSQL -e INTEGRATIONDB_MYSQL_SERVICE_HOST=mysql -e INTEGRATIONDB_MYSQL_SERVICE_PORT=3306    
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

Access the application 
---------------------

    curl http://eap-messaging-workshop-middleware.apps.nodisk.space/messaging/rest/sender/send\?text\=testo

    curl -X POST -H "Content-Type: application/json" -d '{"id":5,"name":"Daniele","surname":"Di Bartolomei"}' http://eap-messaging-workshop-middleware.apps.nodisk.space/messaging/rest/sender/addAccount
    curl -X POST -H "Content-Type: application/json" -d '{"id":103,"item":"New Order","amount":2,"description":"A New Fresh Order","processed": 0}' http://eap-messaging-workshop-middleware.apps.nodisk.space/messaging/rest/sender/addOrder


Investigate the Server Console Output
-------------------------

    oc logs -f dc/eap-messaging