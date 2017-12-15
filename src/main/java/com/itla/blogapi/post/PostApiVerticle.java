/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.post;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 *
 * @author hectorvent
 */
public class PostApiVerticle extends AbstractVerticle {

    private final static String PATH = "/posts";
    private final static String PATH_ID = PATH + "/:postId";
    private final PostService postService;

    public PostApiVerticle(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        // servers method
        router.get(PATH).handler(this::getPosts);
        router.post(PATH).handler(this::addPost);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(80, ar -> {
                    if (ar.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });
    }

//    private void getServer(RoutingContext context) {
//
//        String serverId = context.request().params().get("serverId");
//
//        postService.getPost(serverId, res -> {
//            if (res.succeeded()) {
//                ServerOptions ami = res.result();
//                context.response().setStatusCode(200)
//                        .putHeader("Content-Type", "application/json")
//                        .end(ami.toString());
//            } else {
//                context.response().setStatusCode(404)
//                        .putHeader("Content-Type", "application/json")
//                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
//            }
//        });
//    }


    private void getPosts(RoutingContext context) {

        postService.getPosts(res -> {
            if (res.succeeded()) {
                JsonArray posts = new JsonArray();
                res.result().stream().map(Post::toJson).forEach(posts::add);
                context.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(posts.encode());
            } else {
                context.response().setStatusCode(500)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).encode());
            }
        });

    }

    private void addPost(RoutingContext context) {

        Post ami = new Post(context.getBodyAsJson());

        postService.addPost(ami, res -> {
            if (res.succeeded()) {
                context.response().setStatusCode(201)
                        .putHeader("Content-Type", "application/json")
                        .end(ami.toJson().toString());
            } else {
                context.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
            }
        });

    }

}
