## Binary deploy app

If you have it delete all old stuff:

```
oc delete bc/account-service dc/account-service svc/account-service route/account-service
```

```
oc new-build --image-stream=openshift/fuse7-java-openshift:1.4 --name=account-service --binary=true
oc start-build account-service --from-dir=target/account-service-1.0.jar
oc new-app account-service -e MYSQL_SERVICE_NAME='mysql' -e MYSQL_SERVICE_USERNAME='mysql' -e MYSQL_SERVICE_PASSWORD='mysql' -e MYSQL_SERVICE_DATABASE=integrationdb
```

Create the service

```
oc delete svc/account-service
oc create -f account-service-svc.yaml
```

Create the route

```
oc create -f account-service-route.yaml
```
