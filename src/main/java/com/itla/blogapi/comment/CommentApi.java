/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.comment;

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
public class CommentApi extends Authenticate {

    private final static String PATH = "/comments";
    private final CommentService commentService;

    public CommentApi(CommentService postService, LocalMap<String, Integer> tokens) {
        super(tokens);
        this.commentService = postService;
    }

    public void start(Router router) throws Exception {

        router.get(PATH).handler(this::getComments);
        router.post(PATH).handler(this::addPost);
    }

    private void getComments(RoutingContext context) {

        authenticate(context, r -> {
            Map<String, String> params = new HashMap<>();

            String sPostId = context.request().params().get("postId");

            commentService.getComments(Integer.valueOf(sPostId == null ? "0" : sPostId), res -> {
                if (res.succeeded()) {
                    JsonArray posts = new JsonArray();
                    res.result().stream().map(Comment::toJson).forEach(posts::add);
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

        Comment post = new Comment(context.getBodyAsJson());

        commentService.addComment(post, res -> {
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

    }

}
