COMPOSE = docker compose -f infra/docker-compose.yml --env-file .env

setup-certs:
	@bash infra/scripts/setup-certs.sh

.PHONY: rebuild up down restart logs ps build clean setup-certs

rebuild: setup-certs
	$(COMPOSE) up -d --build

up: setup-certs
	$(COMPOSE) up -d

down:
	$(COMPOSE) down

restart:
	$(COMPOSE) down
	$(COMPOSE) up -d --build

logs:
	$(COMPOSE) logs -f --tail=200

ps:
	$(COMPOSE) ps

build:
	$(COMPOSE) build

clean:
	$(COMPOSE) down -v
