#!/bin/bash
cd ../
mvn clean install
cd deploy-banking-app
docker compose up -d
