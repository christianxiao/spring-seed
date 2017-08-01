# Spring-seed
A seed project based on Spring Framework family, including Spring Data JPA, Spring Data Redis and so on.

## Why this project
During Java web development, we usually have to work on several projects at the same time and we try to make those applications follow the same structure,
including directory structure, annotation based configuration over xml based configuration, naming convensions and so on.  
This is really useful when we need to create a new project. For example, the new project should share the same directory structure, third party dependencies and version (Spring, Hibernate, Redis..),
build scripts & tools and so on.  
So I try to create a common seed project including all those things to avoid repeating.

## The quick structure overview
- Just like other Spring frameworks, annotation based configuration is heavily used, like @Enable-* annotations.
- The build tool is gradle. Of course you can use Maven but then you need to write your own build scripts instead of those provided.
- Third party dependencies including Spring Framework 4, Hibernate, Spring Data JPA, Spring Data Redis, Spring Batch, Apache commons, Guava.
- Common J2EE applications include batch job module, domain module, web module. Spring seed have special support for those modules.
- Configure Swagger2 to check and test all Rest Apis.
- Thanks to docker, we don't need to install Mysql, Redis and config them locally, just install docker and run those shell scripts. Then your Mysql and redis is ready.

## Jcenter hosted
This project is hosted on Jcenter:
- Gradle
```
compile 'com.profullstack:spring-seed:1.0.0'
```
- Maven
```
<dependency>
  <groupId>com.profullstack</groupId>
  <artifactId>spring-seed</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## Run this project is damn easily 
We provide docker shell script to help you run this project, but it is MacOS based, but I guess it will be easy to adapt to Windows.
- Install Docker and git.
- Git clone this project and go to sample folder.
```
git clone https://github.com/profullstack/spring-seed
cd spring-seed/sample
```
- Run Mysql docker, the database and database users will be automatically created. Note: you need to stop your local mysql first in case of port 3306 conflict.
```
cd deploy/mysql
./mysql-docker-start.sh
#Will print the unique docker instance ID
#Now an empty Mysql datase and two users dml and ddl is created.
```
- Run Redis cluster docker. Note: you need to stop all your local redis instances if you have any.
```
cd ../redis-cluster/
./redis-cluster-start.sh
#Will print docker instances process
```
- Migrate datase structure and insert test data with Flyway.
```
cd ../..
./gradlew :domain:flywayMigrate -Dflyway.extLocation=src/migration/data
#Will print success at the end.
#You can use any Mysql client to check the database. The root password is root.
```
- Finally, run our tomcat server.
```
./gradlew web:tomcatRun
```
- Go to http://localhost:8080/api/swagger-ui.html to check and test all the Rest Apis.

## Implementation details
