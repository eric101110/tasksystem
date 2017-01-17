<h1>Tasksystem</h1>

Trello similar project with two separated parts.

Tasksystem-Data is the Data/Service layer build with Spring using configuration beans to autowire the repositories.
This layer is used for managing storing of the data in MYSQL using Hibernate(ORM) to convert models to database entries and handling user input with services.<br>
The service layer is unit tested with HSQLDB Inmemory database.

Tasksystem-API is a Restful-API built using Jax-rs(Jersey) where you can manage the system through HTTP requesting endpoints with parameters or methods.<br>
All the endpoints are tested using Spring boot's RestTemplate and mocking the service layer.


I collaborated with [Patrik Ekdahl](https://github.com/patrix11)
