# 3.2.2

Pin the datastore client to the HTTP transport. Version 3.x of the client library switched the default to gRPC, whose netty direct-memory pools and thread pools pushed the F1 instance past its 384 MiB limit after roughly 100 requests. Also route the cross-namespace read through the shared client cache instead of creating unclosed clients per call.

# 3.2.1

Reuse one datastore client per tenant namespace instead of creating a new client on every request. Fixes instances running out of memory after roughly 100 requests.

# 3.2.0

Remove warmup endpoint. Update the cloud datastore dependency.

# 3.1.0

Fix simple date format issue, and large list/map serialization issue. Update dependencies.

# 3.0.0 

Found the issue with metric tracing and have removed it. This should stop the noisy errors in the logs.

# 2.9.1

Attempting to stop metric tracing which is erroring and noisy.

# 2.9.0

Upgrade to Micronaut 5 and Java 25.

# 2.8.0

Update dependencies and fix always on for this API.

# 2.7.0

Attempting always on for this API.

# 2.6.2

Add authorize audience to all controller.

# 2.6.1

Update dependencies.

# 2.6.0

Add fine grained permissions to endpoints.

# 2.5.0

Add deploy and acceptance test events

# 2.4.0

Update micronaut and dependency versions. Fix a bug with the sort controller.

# 2.3.0

Update micronaut and dependency versions

# 2.2.0

Add an `all` controller, which allows visibility into data across tenants for administrative operations.

# 2.1.0

Enable multi-tenancy

# 2.0.0

Move to micronaut, java 17. Change the host to https://datastore.data.trevorism.com

# 1.6.0

Update to github actions

# 1.5.0

Added object endpoints to match api. We will transition to object in version 2.0.0 as it fits the data.trevorism.com interface pattern.
Implemented native filtering, sorting, and paging

# 1.4.0

Reimplemented datastore to use latest google library. Made performance improvements.

# 1.3.1

Made https a requirement

# 1.3.0

Completed security model
