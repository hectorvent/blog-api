/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.post;

import com.itla.blogapi.Authenticate;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hectorvent
 */
public class PostApi extends Authenticate {

    private final static String PATH = "/posts";
    private final static String PATH_ID = PATH + "/:postId";
    private final PostService postService;

    public PostApi(PostService postService, LocalMap<String, Integer> tokens) {
        super(tokens);
        this.postService = postService;
    }

    public void start(Router router) throws Exception {

        router.get(PATH).handler(this::getPosts);
        router.get(PATH_ID).handler(this::getPost);
        router.post(PATH).handler(this::addPost);
    }

    private void getPost(RoutingContext context) {

        authenticate(context, r -> {
            String postId = context.request().params().get("postId");

            postService.getPost(Integer.valueOf(postId), res -> {
                if (res.succeeded()) {
                    Post post = res.result();
                    context.response().setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(post.toJson().encode());
                } else {
                    context.response().setStatusCode(404)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
                }
            });
        });
    }

    private void getPosts(RoutingContext context) {

        authenticate(context, r -> {
            Map<String, String> params = new HashMap<>();

            for (Map.Entry<String, String> param : context.request().params()) {
                params.put(param.getKey(), param.getValue());
            }

            postService.getPosts(params, res -> {
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
        });

    }

    private void addPost(RoutingContext context) {

        authenticate(context, r -> {

            Post post = new Post(context.getBodyAsJson());
            post.setUserId(r.result());

            postService.addPost(post, res -> {
                if (res.succeeded()) {

                    post.setId(res.result());
                    context.response().setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(post.toJson().toString());
                } else {
                    context.response().setStatusCode(400)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
                }
            });
        });
    }

}
