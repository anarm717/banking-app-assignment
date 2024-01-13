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

To run services in docker : 
- Run deploy.sh in deploy-banking-app folder. It will create porstres database and containers for each service.


Here is main methods for operations:

- Get auth JWT token first need to login(created test user name : admin, password : Admin@123): http://localhost:8081/swagger-ui/index.html#/auth-controller/login 

- Create customer : http://localhost:8082/swagger-ui/index.html#/Customer/add

    input parameters : {
                        "name": "NameParam",
                        "surname": "SurnameParam",
                        "birthDate": "01.01.1988",
                        "gsmNumber": 505005050,
                        "balance": 100
                        }
    This method will create customer and gsm number in customer database with zero balance and calls topUp method with initial amount on payment service. After top up is successfully finished topUp method will call add-balance method from Customer service and will  add initial amount to customer balance.

- Top up balance by gsmNumber : http://localhost:8083/swagger-ui/index.html#/Payment/topUp

    input parameters: { "topUpAmount":50 } 
    After create top up transaction in payment database, will call add-balance method from Customer service and will add top up amount to customer balance.

- Purchase by gsmNumber : http://localhost:8083/swagger-ui/index.html#/Payment/purchase

    input parameters: { "purchaseAmount":50 } 
    After create purchase transaction in payment database, will call subtract-balance method from Customer service and will subtract purchase amount to customer balance.

- Refund by purchase id: http://localhost:8083/swagger-ui/index.html#/Payment/refund

    input parameters: { "refundAmount":50 } 
    After create refund transaction and refund details (purchase transaction id and refund transaction) in payment database, will call add-balance method from Customer service and will add refund amount to customer balance.

- View transaction by id: http://localhost:8083/swagger-ui/index.html#/Payment/getByUUId

- View transactions by gsmNumber: http://localhost:8083/swagger-ui/index.html#/Payment/getByGsmNumber

    Pagination added for this method


Checks and validations

- Check gsm number exists before topUp, purchase balance
- Check initial balance is minimum 100
- Rollback if paymentService error when add customer
- Rollback if customerService error when top-up balance
- Rollback if customerService error when purchase balance
- Rollback if customerService error when refund transaction
- Check transaction id exist and type is purchase when refund
- Check refund amount lower than purchase amount
- Refund amount should be lower than purchase amount subtract sum of old refund amounts 
- Unique index added for active gsm numbers
- Unit tests created for payment service
