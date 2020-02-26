[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f20287f815d9438cb6b107c999ddc6a5)](https://app.codacy.com/gh/rednavis/maas-api?utm_source=github.com&utm_medium=referral&utm_content=rednavis/maas-api&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.com/rednavis/spring-graphql-microservice.svg?branch=master)](https://travis-ci.com/rednavis/maas-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7d36295503574b40bb06bd4975dc40f6)](https://app.codacy.com/gh/rednavis/maas-api?utm_source=github.com&utm_medium=referral&utm_content=rednavis/maas-api&utm_campaign=Badge_Grade_Settings)
[![codecov](https://codecov.io/gh/rednavis/spring-graphql-microservice/branch/master/graph/badge.svg)](https://codecov.io/gh/rednavis/maas-api)

# maas-api
Server side API of the Material Assets management system.

## Technology stack
- Java 12
- Spring Boot
- Spring Data
- Spring Security
- Gradle
- Log4j
- Lombok
- Swagger

## Clone project
`git clone git@github.com:rednavis/maas-api.git`

`cd mass-api`

`git submodule update --init`

## Build project
`./gradlew clean build`

`./gradlew bootRun`

## Create dependency report
`./gradlew clean htmlDependencyReport`

## Create docker image
`./gradlew clean bootJar jibDockerBuild`

## Swagger
http://localhost:8081/swagger-ui.html

## API Documentation

- [Signup](#signup)
    - [Success](#signup-success)
    - [Email already exists](#signup-email-already-exists)
    - [Bad request](#signup-bad-request)
- [Signin](#signin)
    - [Success](#signin-success)
    - [Email not found](#signin-email-not-found)
    - [Unauthorized](#signin-unauthorized)
- [Current user](#current-user)
    - [Success](#current-user-success)
    - [Unauthorized](#current-user---unauthorized)

## Signup

```
POST: http://localhost:8081/api/auth/signup
```

Header
```
Content-Type: application/json
```

Body
``` json
{
	"name": "lesharb",
	"email": "lesharb@gmail.com",
	"password": "password"
}
```

## Signup success 
Status: 200

Body (Created user id)
```
1
```

## Signup email already exists
Status: 409

Body (Created user id)
```
{
  "code": 409,
  "message": "Email is already taken"
}
```

## Signup bad request
Status: 400

Body
```
{
  "code": 400,
  "message": "error message..."
}
```

***

## Signin
```
POST: http://localhost:8081/api/auth/signin
```

Header
```
Content-Type: application/json
```

Body
``` json
{
	"email": "lesharb@gmail.com",
	"password": "password"
}
```

## Signin success 
Status: 200

Body
```
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTQxNTA2MjA4LCJleHAiOjE1NDIxMTEwMDh9.xRU2vmXUvtHmrL_BDKm-rzTKd0Sv8BtPR3AmOO0ZgLnSXIT0EeDh1cN7lvrxM-H2fNuO4vJMJdOXV8By8E4BkQ",
  "tokenType": "Bearer"
}
```

## Signin email not found
Status: 404

Body
```
{
  "code": 404,
  "message": "User not found [email: tut@gmail.com]"
}
```

## Signin unauthorized 
Status: 401

Body
```
{
  "timestamp": "2020-02-20T16:16:22.136+0000",
  "status": 401,
  "error": "Unauthorized",
  "message": "Sorry, You're not authorized to access this resource.",
  "path": "/api/auth/signin"
}
```
***

## Current user
Logged in user credentials

```
GET: http://localhost:8081/api/users/me
```

Header
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTQxNTA2MjA4LCJleHAiOjE1NDIxMTEwMDh9.xRU2vmXUvtHmrL_BDKm-rzTKd0Sv8BtPR3AmOO0ZgLnSXIT0EeDh1cN7lvrxM-H2fNuO4vJMJdOXV8By8E4BkQ
```

## Current user success 
Status: 200

Body
```
{
  "id": 1,
  "username": "lesharb@gmail.com",
  "name": "lesharb"
}
```

## Current user - unauthorized 
Status: 401

Body
```
{
  "timestamp": "2020-02-20T16:26:42.886+0000",
  "status": 401,
  "error": "Unauthorized",
  "message": "Sorry, You're not authorized to access this resource.",
  "path": "/api/users/me"
}
```