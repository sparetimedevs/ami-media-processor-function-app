#!/bin/bash

docker-compose -f ./infra/azurite/docker-compose.yml up -d

mvn clean package -DskipTests

mvn azure-functions:run -DenableDebug
