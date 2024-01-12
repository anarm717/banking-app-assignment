#!/bin/bash
mvn -f ../auth-service/pom.xml clean install
mvn -f ../customer-service/pom.xml clean install
mvn -f ../payment-service/pom.xml clean install -DskipTests
docker compose up -d
