# Blog API Backend

[![GitHub version](https://badge.fury.io/gh/hectorvent%2Fblog-api.svg)](https://badge.fury.io/gh/hectorvent%2Fblog-api)
[![Docker Cloud Automated build](https://img.shields.io/docker/cloud/automated/hectorvent/blogapi?style=flat-square)](https://hub.docker.com/r/hectorvent/blogapi)
[![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/hectorvent/blogapi?style=flat-square)](https://hub.docker.com/r/hectorvent/blogapi/builds)

Blog API is a backend API that is fully implemented using [vertx.io](https://vertx.io/). Vertx is a Reactive Java base toolkit, that brings enough performance to Java Apps.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/68226ed67f948f926225)

> :warning: This implementation was used as Educational Resource on the **Instituto Tecnológico de las Americas (ITLA)**, where I was a Teacher for two years [2017-2019].

Used technologies:
- [Vertx.io](https://vertx.io/): It's a toolkit to building reactive applications on the JVM.
- [Docker](https://www.docker.com/): Linux Container where the app in bundle.
- [Docker Compose](https://github.com/docker/compose): To deploy the App stack (BlogAPI and MariaDB).
- [MariaDB](https://mariadb.org/): SQL Database where all data are stored.

### What can this API:

* Manage register/login :key:
* Create Posts :newspaper:
* Create Comments :mega:
* Likes posts :+1:
* Events with through Websocket:
  * when a new user is connected/disconnected :electric_plug:
  * when a new post/comment are created 
  * when a user likes/dislikes a post.

### Demo:

> :warning: This demo will be available soon!.

> :warning: Please use fake `username` and `password` to register.

Click [here](https://blogapi-gui.hectorvent.com) to see the GUI, that has implemented this [API](https://blogapi.hectorvent.com).

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

