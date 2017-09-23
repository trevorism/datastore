# Datastore
CRUD operations on objects via a REST API.

Objects have the following rules:
* Must have an omitted, String, or long 'id' parameter
* Can contain strings, numbers, dates, lists, and maps
* Lists can contain other objects, as long as they conform to the above rules

Any operation that costs $$ is secured. You'll need to authorize the request to use it.

Deployed at [Datastore](datastore.trevorism.com)

