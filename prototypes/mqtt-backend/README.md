# mqtt-backend
## Nav
[![Home](../../images/home.ico)](https://github.com/htl-leonding-project/rocketman/blob/master/README.md)
# Source: 
https://github.com/QuirinEcker/quarkus-mqtt

## Running the application in dev mode

Start MQTT the Broker
```shell script	
docker-compose -f docker-compose.mqtt.yml up	
```

Dev mode:
```shell script
./mvnw compile quarkus:dev
```

To submit test data use MQTT Explorer see [here](../../asciidocs/CanSat_mqtt.adoc)

## Swagger documentation

Documentation of all the Endpoints (When the program is started)

http://localhost:8080/q/swagger-ui/

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `rocketman-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/rocketman-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/rocketman-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# RESTEasy JSON serialisation using Jackson

<p>This example demonstrate RESTEasy JSON serialisation by letting you list, add and remove quark types from a list.</p>
<p><b>Quarked!</b></p>

Guide: https://quarkus.io/guides/rest-json
