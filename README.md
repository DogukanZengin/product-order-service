Product And Order Management Application
----------------------------------------

This is a simple service for managing products and orders. Developed for assignment purposes.

Prerequisites
-------------
- Docker Desktop must be installed -  https://www.docker.com

Build
-----
- Clone the project to your local environment

Option 1 (Mysql will be installed and used)
- Make sure docker is running
- Run "docker-compose up" command in the project directory
- Docker image can also be pulled from my repo - `docker pull dogukanzengin/default:latest`

Option 2 (H2 Embedded DB is used)
- In the project directory run `./mvnw spring-boot:run`

Usage
---------------
- Base Path for the application is `http://localhost/api/v1`
- Documentation for REST APIs can be found on  following URL
        http://localhost:8080/api/v1/swagger-ui.html
- Sample requests are listed on a postman collection
        https://www.getpostman.com/collections/a58eb6ace59476df688c
        __Download__ : https://www.postman.com
- DB Console can be accessed via http://localhost:8080/api/v1/h2-console/login.jsp
    .No password required, JDBC Url is `jdbc:h2:file:~/mg-db` (Only for Option 2)
- Data store will contain 3 Product populated upfront
        
Ideas for Authentication
------------------------
 - Authentication for REST APIs can be achieved with Spring Security and Json Web Tokens(JWT).
 Users will be passed a token after successful login and that token will be used in request headers for
 REST api requests. Token will have a expire date, which can be refreshed with another refresh token passed during login.
 Third party authentication servers like RedHat's KeyCloak can also be used for creating a mechanism easily.
 
Ideas for Redundancy
--------------------
- To make the service highly available, vertical and horizontal scaling properties must be examined.
By increasing hardware specs of the server vertical scaling is achieved and server can handle more traffic.
But in order to have a failover system, horizontal scaling must be considered by adding multiple servers behind a load balancer.
Cloud providers like AWS or GCP also use auto scaling and region features to have a redundant system. DB Servers must be external to application
and replicated to different regions. Keeping state in the application could cause loss of data, so creating a stateless architecture must be considered carefully

 
 
    
