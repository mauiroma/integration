# Build

```mvn clean package -DskipTests```

You need the Red Hat repository in your m2


# Run locally

```mvn spring-boot:run```

# Setup OCP

You need to be logged in the cluster with ```oc login```

## MySQL DB


## Binary deploy app

If you have it delete all old stuff:

```
oc delete bc/product-service dc/product-service svc/product-service
```

```
oc new-build --image-stream=openshift/fuse7-java-openshift:1.4 --name=product-service --binary=true
oc start-build product-service --from-dir=target/product-service-1.0.jar
oc new-app product-service -e MYSQL_SERVICE_NAME='mysql' -e MYSQL_SERVICE_USERNAME='mysql' -e MYSQL_SERVICE_PASSWORD='mysql' -e MYSQL_SERVICE_DATABASE=integrationdb
```

Create the service

```
oc delete svc/product-service
oc create -f product-service-svc.yaml
```

Create the route

```
oc create -f product-service-route.yaml
```

# Test the app

## Insert an order

curl -X POST http://<YOUR_HOST>:<YOUR_PORT>/product-service/products/placeorder

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"id":100,"item":"My order","amount":1,"description":"a Sample post order","processed":false}' \
  http://`oc get route/product-service -o jsonpath='{.spec.host}'`/product-service/products/placeorder

## Get order

curl -X GET http://`oc get route/product-service -o jsonpath='{.spec.host}'`/product-service/products/order/100

## Get the list

```curl -X GET http://`oc get route/product-service -o jsonpath='{.spec.host}'`/product-service/products```
