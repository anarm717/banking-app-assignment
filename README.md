# Banking Application Assignment for Backend Developer Interview

This is a simple banking backend application written in Java, designed to provide basic functionalities for managing customer accounts. The application supports customer creation, balance management, and transaction processing through either a Rest API or a basic GUI. The application is built with microservice architecture principles and stores data securely in a relational database (PostgreSQL).

Here is requirements


Please write a Small Banking Backend Application with the below functionalities

• Create a customer with fields Name, Surname, Birth Date, GSM Number, and Balance.

• The initial customer balance should start with 100 azn.

• Customers can make top-up money, purchase, and refund transactions via Rest API or basic GUI.

• Create database and store data in a database (Oracle, PostgreSQL, MySQL).

• The communication channel must be secure.

• The solution must be written in Java language and compatible with microservice architecture.


Please don't:

• Create a fancy UI.

• Images are not important.


Demonstration Steps:

• Top-up 15 azn to Customer balance.

• Purchase 10 azn from Customer balance.

• Refund 5 of 15 azn from the last purchase transaction.


Architecture

![Alt text](banking_app_architecture.jpg)


Technology Stack:

• Java language.

• Spring Boot framework.

• Spring Data JPA.

• Maven - Dependency Management

• Swagger for API documentation.

• JWT for authentication services.

• Microservice architecture.

• PostgreSQL for database

• Docker

• To create API documentation in JavaDoc you can run this command for each service : mvn javadoc:javadoc


URLS:


Auth API : http://localhost:8081/swagger-ui/index.html

Customer API : http://localhost:8082/swagger-ui/index.html

Payment API : http://localhost:8083/swagger-ui/index.html


Installation:

- You need to install postgreSQL server or you can run in docker 
- Simple command for build postgreSQL in docker : docker run –name pgsql-dev -e POSTGRES_PASSWORD=Welcome4$ -p 5432:5432 Postgres

To run services in docker : 
- Build docker image for each service : 
    - mvn clean install
    - docker build -t {service_name} .
    - docker container run --name {service_name} -it -d -p {port}:{port} {service_name}


Here is main methods for operations:

- Get auth JWT token first need to login(created test user name : admin, password : Admin@123): http://localhost:8081/swagger-ui/index.html#/auth-controller/login 

- Create customer : http://localhost:8082/swagger-ui/index.html#/Customer/add

- Top up balance by gsmNumber : http://localhost:8083/swagger-ui/index.html#/Payment/topUp

- Purchase by gsmNumber : http://localhost:8083/swagger-ui/index.html#/Payment/purchase

- Refund by purchase id: http://localhost:8083/swagger-ui/index.html#/Payment/refund

- View transaction by id: http://localhost:8083/swagger-ui/index.html#/Payment/getByUUId

- View transactions by gsmNumber: http://localhost:8083/swagger-ui/index.html#/Payment/getByGsmNumber

