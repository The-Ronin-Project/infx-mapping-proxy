# Kotlin BluePrint Template

Simple project using a class called "Student" to demonstrate simple kotlin springboot project that can do a simple "GET" and "POST"
* includes database table setup
* demonstrates using authorization via SEKI
* handles basic validation

# Local Development

See [Standard Service Development](https://projectronin.atlassian.net/l/cp/wNJKyaEY) for details on how to build and
run for local development work.

# Ronin Blueprint Service

## API

**NOTE**: _With the introduction of swagger, some of the sections in this README are semi-redundant and subject for removal in the near future._

### UI

`http(s)://host:port/swagger-ui/index.html`

### Schema

`http(s)://host:port/v3/api-docs`

## Modules

- `ronin-blueprint-database` contains the database migrations for the service.
- `ronin-blueprint-service` is the server component of the service.

## Sample Requests

Here's an example request to create a new Student Record

### Via CURL command

```shell
curl -v --request POST --location 'http://localhost:8080/api/student' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NTMzMjgzMjAsImlzcyI6IlNla2kiLCJqdGkiOiIycm9zcW05M2VlbmFwYmlrZm8wMXFrODEiLCJzdWIiOiIxNTFhMjUwOS1lNjllLTQwNDMtYmJhOC1kYmY5ODhkZGE1NTUiLCJ0ZW5hbnRpZCI6ImFwcG9zbmQifQ.gmX_Ad6sgTTW0iogI4kwuhYYbnpn5HGIE5RZxi56Ojs' \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{
        "firstName": "Jim",
        "lastName": "Muffet"
      }'
```

### Via http request file (IntellJ-specific)

Once the service is running you can run a simple canned request from the IntelliJ IDE in the file [studentrequests.http](ronin-blueprint-service/src/test/resources/studentrequests.http)
Add a file `http-client.private.env.json` next to [http-client.env.json](ronin-blueprint-service/src/test/resources/http-client.env.json) and specify your `bearerToken` and `studentId` variables there.
In the Run With dropdown select your environment.

### Declarative Deployment

self-service - https://github.com/projectronin/devops-self-service-tf/tree/main/projects/ronin-blueprint
ronin-charts - https://github.com/projectronin/ronin-charts/tree/master/ronin-blueprint
ronin-deployment - https://github.com/projectronin/ronin-deployments/tree/master/apps/ronin-blueprint
