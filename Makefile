.PHONY: default test help

default: help

# COLORS
GREEN  := $(shell tput -Txterm setaf 2)
YELLOW := $(shell tput -Txterm setaf 3)
WHITE  := $(shell tput -Txterm setaf 7)
RESET  := $(shell tput -Txterm sgr0)


TARGET_MAX_CHAR_NUM=20
# Show this help.
help:
	@echo ''
	@echo 'Usage:'
	@echo '  ${YELLOW}make ${GREEN}<target>${RESET}'
	@echo ''
	@echo 'Targets:'
	@awk '/^[a-zA-Z\-\_0-9]+:/ { \
		helpMessage = match(lastLine, /^## (.*)/); \
		if (helpMessage) { \
			helpCommand = substr($$1, 0, index($$1, ":")-1); \
			helpMessage = substr(lastLine, RSTART + 3, RLENGTH); \
			printf "  ${YELLOW}%-$(TARGET_MAX_CHAR_NUM)s${RESET} ${GREEN}%s${RESET}\n", helpCommand, helpMessage; \
		} \
	} \
	{ lastLine = $$0 }' $(MAKEFILE_LIST)

## Create Docker Image
create-image:
	# Security Module
	@echo "${YELLOW}1- Creating hectorvent/blogapi image${RESET}"
	@docker build -t hectorvent/blogapi .


## Build Project -> [mvn clean install]
build:
	docker run -it --rm \
	     -u $(shell id -u):$(shell id -g) \
		 -v $(shell pwd):/usr/src/app -v $(HOME)/.m2:/var/maven/.m2 \
		 -e MAVEN_CONFIG=/var/maven/.m2 -w /usr/src/app \
		 maven:3.5.4-jdk-8-alpine mvn -Duser.home=/var/maven clean install -DskipTests=true

