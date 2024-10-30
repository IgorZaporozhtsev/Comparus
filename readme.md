# How to use application
 - use [docker-compose.yml](docker-compose.yml) to quicker configure databases
 - [docker-compose.yml](docker-compose.yml) will run two postgres containers in different ports to simulate different data sources
 - you can see [application.yml](src%2Fmain%2Fresources%2Fapplication.yml) file where configured data sources for startup
 - OpenAPI path is [Swagger](http://localhost:8080/swagger-ui.html)
 - Don't forget stop docker containers before run integrations test [UserControllerTest.java](src%2Ftest%2Fjava%2Fcom%2Fzeecoder%2Fcomparus%2FUserControllerTest.java)
