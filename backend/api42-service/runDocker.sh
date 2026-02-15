#!/bin/bash

docker build -t ap42-service .
docker run --env-file .env -p 4444:4444 api42-service
