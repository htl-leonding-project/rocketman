Quarkus MQTT Quickstart
========================

This project illustrates how you can interact with MQTT using MicroProfile Reactive Messaging.

## MQTT server

First you need a MQTT server. You can follow the instructions from the [Eclipse Mosquitto](https://mosquitto.org/) or run `docker-compose up` if you have docker installed on your machine.

## Start the application

The application can be started using: 

```bash
mvn quarkus:dev
```  

Then, open your browser to http://localhost:8080/rocketman.html, and you should see a fluctuating price.

## Anatomy

In addition to the `rocketman.html` page, the application is composed by 2 component:

* `RocketmanGenerator` - a bean generating random Rocketman Values. They are sent to a MQTT topic
The result is sent to an in-memory stream of data
* `RocketmanResource`  - the `RocketmanResource` retrieves the in-memory stream of data in which the Rocketman Values are sent and send these Rocketman Values to the browser using Server-Sent Events.

The interaction with MQTT is managed by MicroProfile Reactive Messaging.
The configuration is located in the application configuration.

## Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

and run with:

`./target/mqtt-quickstart-1.0-SNAPSHOT-runner` 