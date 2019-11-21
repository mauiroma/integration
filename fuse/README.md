# OCP Integration Workshop

# Initial setup

## Database

A Mysql is required for the example, use the yaml ```mysql-integrationdb-service.yml``` file in  ```integration-db``` folder in the root of workshop.

Run this command:

```
oc create -f mysql-integrationdb-service.yml
```

This will create a Deployment config in current namespace for MySQL and the   OCP Service

# Integration Examples

## Fuse Rest (fuse-rest)

## Fuse Webservice (fuse-webservice)

