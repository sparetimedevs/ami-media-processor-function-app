#!/bin/bash

mvn clean package -DskipTests

mvn azure-functions:run -DenableDebug
