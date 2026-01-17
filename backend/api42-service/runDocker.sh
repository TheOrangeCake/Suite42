#!/bin/bash

docker run --env-file .env -p 4444:4444 api42-service:latest
