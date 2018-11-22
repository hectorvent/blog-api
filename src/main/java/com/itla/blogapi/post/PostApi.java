package com.itla.blogapi.post;

import com.itla.blogapi.user.User;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hectorvent@gmail.com
 */
public class PostApi {

    private final static String PATH = "/post";
    private final static String PATH_ID = PATH + "/:postId";
    private final static String PATH_ID_LIKE = PATH_ID + "/like";
    private final static String COMMENTS = PATH_ID + "/comment";

    private final PostService postService;
    private final CommentService commentService;

    public PostApi(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    public void start(Router router) throws Exception {

        // post
        router.get(PATH).handler(this::getPosts);
        router.get(PATH_ID).handler(this::getPost);
        router.post(PATH).handler(this::addPost);

        // /post/{id}/comments
        router.get(COMMENTS).handler(this::getComments);
        router.post(COMMENTS).handler(this::addComment);

        // /post/{id}/like
        router.put(PATH_ID_LIKE).handler(this::addLike);
        router.delete(PATH_ID_LIKE).handler(this::removeLike);

    }

    private void getPost(RoutingContext context) {

        String postId = context.request().params().get("postId");
        User user = context.get("user");
        postService.getPost(user, Integer.valueOf(postId), res -> {
            if (res.succeeded()) {
                Post post = res.result();
                context.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(post.toJson().encode());
                postService.incremetView(post, rs -> {
                });
            } else {
                context.response().setStatusCode(404)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
            }
        });
    }

    private void getPosts(RoutingContext context) {

        //      authenticate(context, r -> {
        Map<String, String> params = new HashMap<>();

        for (Map.Entry<String, String> param : context.request().params()) {
            params.put(param.getKey(), param.getValue());
        }
        User user = context.get("user");
        System.out.println("user: " + user.toJson().encodePrettily());
        postService.getPosts(user, params, res -> {
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

        Post post = new Post(context.getBodyAsJson());

        User user = context.get("user");
        post.setUserId(user.getId());

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
    }

    private void getComments(RoutingContext context) {

        String sPostId = context.request().params().get("postId");
        Integer postId = Integer.parseInt(sPostId);

        commentService.getComments(postId, res -> {
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

    }

    private void addComment(RoutingContext context) {

        String sPostId = context.request().params().get("postId");
        Integer postId = Integer.parseInt(sPostId);
        User user = context.get("user");

        postService.getPost(user, postId, res -> {
            if (res.succeeded()) {

                Post post = res.result();

                Comment comment = new Comment(context.getBodyAsJson());
                comment.setUserId(user.getId());
                comment.setPostId(postId);

                commentService.addComment(comment, res1 -> {
                    if (res.succeeded()) {

                        comment.setId(res1.result());
                        context.response().setStatusCode(201)
                                .putHeader("Content-Type", "application/json")
                                .end(comment.toJson().toString());
                    } else {
                        context.response().setStatusCode(400)
                                .putHeader("Content-Type", "application/json")
                                .end(new JsonObject().put("error", true).put("message", res1.cause().getMessage()).toString());
                    }
                });
            } else {
                context.response().setStatusCode(404)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
            }
        });

    }

    private void addLike(RoutingContext context) {

        String sPostId = context.request().params().get("postId");
        Integer postId = Integer.parseInt(sPostId);
        User user = context.get("user");
        postService.getPost(user, postId, res -> {
            if (res.succeeded()) {

                Post post = res.result();
                postService.addLike(user, post, res1 -> {
                    if (res.succeeded()) {
                        context.response().setStatusCode(200)
                                .putHeader("Content-Type", "application/json")
                                .end("");
                    } else {
                        context.response().setStatusCode(400)
                                .putHeader("Content-Type", "application/json")
                                .end(new JsonObject().put("error", true).put("message", res1.cause().getMessage()).toString());
                    }
                });
            } else {
                context.response().setStatusCode(404)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
            }
        });

    }

    private void removeLike(RoutingContext context) {
        String sPostId = context.request().params().get("postId");
        Integer postId = Integer.parseInt(sPostId);
        User user = context.get("user");
        postService.getPost(user, postId, res -> {
            if (res.succeeded()) {

                Post post = res.result();

                postService.removeLike(user, post, res1 -> {
                    if (res.succeeded()) {
                        context.response().setStatusCode(200)
                                .putHeader("Content-Type", "application/json")
                                .end();
                    } else {
                        context.response().setStatusCode(400)
                                .putHeader("Content-Type", "application/json")
                                .end(new JsonObject().put("error", true).put("message", res1.cause().getMessage()).toString());
                    }
                });
            } else {
                context.response().setStatusCode(404)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
            }
        });
    }

}
