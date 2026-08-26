# Datastore
![Build](https://github.com/trevorism/datastore/actions/workflows/deploy.yml/badge.svg)
![GitHub last commit](https://img.shields.io/github/last-commit/trevorism/datastore)
![GitHub language count](https://img.shields.io/github/languages/count/trevorism/datastore)
![GitHub top language](https://img.shields.io/github/languages/top/trevorism/datastore)

CRUD operations on objects via a REST API.

Objects have the following rules for creation:
* Must have an omitted, String, or long 'id' parameter
* The id parameter must be numeric
* Can contain strings, numbers, dates, lists, and maps
* Lists can contain other objects, as long as they conform to the above rules

Any operation that costs $ is secured. You'll need to authorize the request to use it.

Supports filtering, sorting, and paging. These are subject to the limitations of the underlying data source, which means
certain operations may fail on unindexed columns. For a full in memory implementation of these operations, see [Trevorism Data](https://github.com/trevorism/data)

Deployed at [Datastore](https://datastore.data.trevorism.com)


## Backup documentation

The datastore lives in the `trevorism-data` project, in the `(default)` database at `us-east4`.
The backup bucket must be in the same location as the database or the export fails.

Create the bucket once:
```
gcloud storage buckets create gs://trevorism-data-backup --project=trevorism-data --location=us-east4
```

Then export:
```
$Bucket = "gs://trevorism-data-backup"
gcloud config set project trevorism-data
gcloud datastore export $Bucket

-or-
gcloud datastore export --kinds="KIND1,KIND2" $Bucket
```

Exports are long running. Add `--async` and track progress with:
```
gcloud datastore operations list --project=trevorism-data
```