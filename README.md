# Http streaming examples

This repository contains examples for passing large data sets with REST API.
Technologies used: Akka Http, Akka Streams, MongoDB.

Use case:
- pass data from database "test" and collection "resources" to user via REST API
- solution should be able to pass collection of any size


## Solution 1: Pagination
Example for Pagination can be found in ApiReturningList.scala object.

## Solution 2: Chunking
Example for Chunking can be found in AppReturningChunking.scala object.


## Test
After running one of examples, make GET request to localhost:8080/resources

```
curl -X GET http://localhost:8080/resources
```

## More information

For more information please refer to blog post.