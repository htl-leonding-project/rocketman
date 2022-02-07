#!/bin/bash

echo "Starting native build..."
./mvnw clean
rm mqtt-backend-v1.0.0-runner

echo "Disabling JSON-Validation"
sed -i '53s/.*/        Set<ValidationMessage> errors = new HashSet<>();/' src/main/java/at/htl/rocketman/MqttConsumer.java

echo "Building native image"
./mvnw clean package -Dquarkus.package.type=native -Dmaven.test.skip=true

echo "Enabling JSON-Validation"
sed -i '53s/.*/        Set<ValidationMessage> errors = schema.validate(node);' src/main/java/at/htl/rocketman/MqttConsumer.java

cp target/*-runner .

./mvnw clean
echo "Finished building, you can now execute mqtt-backend-v1.0.0-runner"
