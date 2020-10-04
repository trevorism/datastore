# Datastore
![Jenkins](https://img.shields.io/jenkins/build/http/trevorism-build.eastus.cloudapp.azure.com/datastore)
![Jenkins Coverage](https://img.shields.io/jenkins/coverage/jacoco/http/trevorism-build.eastus.cloudapp.azure.com/datastore)
![GitHub last commit](https://img.shields.io/github/last-commit/trevorism/datastore)
![GitHub language count](https://img.shields.io/github/languages/count/trevorism/datastore)
![GitHub top language](https://img.shields.io/github/languages/top/trevorism/datastore)

Latest Version: 1.2.0

CRUD operations on objects via a REST API.

Objects have the following rules:
* Must have an omitted, String, or long 'id' parameter
* The id parameter must be numeric
* Can contain strings, numbers, dates, lists, and maps
* Lists can contain other objects, as long as they conform to the above rules

Any operation that costs $$ is secured. You'll need to authorize the request to use it.

Deployed at [Datastore](datastore.trevorism.com)

## Backups
```
$Bucket = "gs://trevorism-gcloud-backup"
gcloud config set project trevorism-gcloud
gcloud datastore export $Bucket

-or-
gcloud datastore export --kinds="KIND1,KIND2" $Bucket
```