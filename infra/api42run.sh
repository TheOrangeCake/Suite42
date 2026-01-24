#!bin/bash

docker compose build api42-postgresql api42-service
docker compose up api42-postgresql api42-service
