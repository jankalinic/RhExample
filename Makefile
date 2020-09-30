.PHONY: all build docker push

all: build docker push

build:
	mvn package

docker:
	docker build -t api-server-image .

push:
	docker tag api-server-image:latest docker.io/jankalinic/api-server:latest
	docker push docker.io/jankalinic/api-server:latest

