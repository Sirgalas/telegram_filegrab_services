docker-up:
	docker-compose up -d

docker-down:
	docker-compose down --remove-orphans

docker-pull:
	docker-compose pull --include-deps

docker-build:
	docker-compose build

docker-ps:
	docker-compose ps

docker-logs:
	docker-compose logs $(filter-out $@, $(MAKECMDGOALS))

docker-down-clear:
	docker-compose down -v --remove-orphans

docker-bash:
	docker-compose exec $(filter-out $@, $(MAKECMDGOALS)) bash

docker-root-sh:
	docker-compose exec -u root $(filter-out $@, $(MAKECMDGOALS)) bash

docker-init: docker-down-clear docker-pull docker-build docker-up