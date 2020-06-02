Blog API
========

Blog API is a backend API fully implemented using [vertx.io](https://vertx.io/), Vertx is a Ractive Java base toolkit, that brings enough performace to our API.

> Note: This implementation was used as Educational Resource on **Instituto Tecnológico de las Americas (ITLA)**, where I was a Teacher for two years [2017-2019].

Used technologies:
- [Vertx.io](https://vertx.io/): Is a tool-kit for building reactive applications on the JVM.
- [Docker](https://www.docker.com/): Linux Container where the app in bundle.
- [Docker Compose](https://github.com/docker/compose): To desploy the App stack (BlogAPI and MariaDB).
- [MariaDB](https://mariadb.org/): SQL Data base were all data are store.

### Demo:

Click [here](https://blogapi-gui.hectorvent.com) to see the GUI, that has implemented this [API](https://blogapi.hectorvent.com). Also you can import the [Postman](https://www.postman.com/) collection from [here](collection here)

User 1:
* username: demo1@blogapi.com
* password: password1

User 2:
* username: demo2@blogapi.com
* password: password2

### How to run?

```
$ cp .env.dist .env
$ nano .env 
$ docker-compose up -d
```
or if you want to build by your own changes.

```shell
$ make build
$ make create-image
$ docker-compose up -d
```

