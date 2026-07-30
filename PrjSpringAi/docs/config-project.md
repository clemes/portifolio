# Creation of the project

The configuration starts defining the dependencies to be included in the project.
[Spring initializr](https://start.spring.io/) simplifies the dependencies definition taking into account which version from SpringBoot used, as well as Java version.
The tool also make sure the version of all dependencies are compatible among them.

The project started with a very basic setup, using only the _Ollama_ dependency.
```xml
<dependency>
	<groupId>org.springframework.ai</groupId>
	<artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
```
When following some of the tutorials, some extra dependencies were added, for example _PGvector Vector Database_.
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-vector-store-pgvector</artifactId>
</dependency>
```
There were other dependencies added after the creation of the project.
The full list of dependencies can be seen in the file [pom.xml](../pom.xml).

## Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# Configuration
The initial setup of the project was as following:

```properties
spring.application.name=PrjSpringAi

#spring.main.web-application-type=none
server.port=${port:8000}

spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=qwen2.5
```
* Defining the name of the application
* Defining a specific server port.
* And defining the Ollama configuration: url to the Ollama server and which model to connect with.

The setup was changed accordingly to which functionality was added to the project.

# Database

The database used in this project was PostgreSql.
Using the PGAdmin tool, it was created the database used in this project.
It was also created a dedicated user for accessing this database, to avoid using the root user configured for PostgreSql.
The database can be created and configured running the script [create_db_user.sql](../db/create_db_user.sql).
```db2
CREATE TABLESPACE ts_springai LOCATION '/data/ts_springai';
CREATE DATABASE db_springai TABLESPACE ts_springai;
CREATE GROUP my_users;
CREATE USER usr_springai WITH ENCRYPTED PASSWORD 'prjspringai' IN GROUP my_users;
GRANT ALL PRIVILEGES ON DATABASE db_springai TO usr_springai;
-- this need to be executed when connected to the database where the user need the privilege.
GRANT ALL ON SCHEMA public TO usr_springai;
```

## Database maven dependency

It was added the following dependencies to the project to support, respectively java persistence API (JPA) and PostgreSQL.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```
* <u>spring-boot-starter-data-jpa</u>: JPA library and Spring data and Hibernate implementation.
* <u>postgresql</u>: includes PostgreSQL JDBC and R2DBC Driver.

_Java Persistence API (JPA)_: is a Java specification for managing relational data in applications using objects instead of writing SQL manually for every operation. It provides (i) map Java classes to database tables, (ii) save, update, delete, and query data, and (iii) handle relationships between entities.

## Database configuration
Following the database setup that enable the application to connect to PostgreSQL.

```properties
# Database properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_springai
spring.datasource.username=usr_springai
spring.datasource.password=prjspringai

# Spring Boot can infer this from the URL
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
