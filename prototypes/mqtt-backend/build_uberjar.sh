#!/bin/bash

echo "Starting uberjar build..."
./mvnw clean
rm mqtt-backend-v1.0.0-runner.jar

echo "Building native image"
./mvnw clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true

cp target/*jar .

./mvnw clean
echo "Finished building"
