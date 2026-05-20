# Java Spring Boot Ecommerce Backend

Simple ecommerce backend application with **Spring Boot**. It's practice and a refresher for me.

In this repository I'm trying to use different things and deal with them, starting with SQLite database configuration,
creating relations between entities, etc.

---

## How To Use
If you want to explore this repository, make sure you have JDK 21 or higher and docker installed 
on your system.

### - First Step 

You need to clone this repository:

    git clone https://github.com/Callmehaan/java_ecommerce_api.git

After that:

    cd java_ecommerce_api/

Now you should install dependencies:

    mvn install

*Optional:* you can run this command to make sure MVN packages are valid:

    mvn verify

### - Second Step
You need **Docker** to run **Redis** on your machine

    docker compose up -d

**For development purposes, I use *the alpine* version of Redis with *no authentication*.**

You can interact with Redis database using this command:

    docker exec -it redis_database redis-cli

### - Third Step
After you start docker container for Redis, run:

    ./mvnw spring-boot:run

Or if you have **Maven** installed globally:

    mvn spring-boot:run

---
