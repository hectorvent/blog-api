package com.itla.blogapi.post;

import com.itla.blogapi.user.User;
import io.vertx.core.Vertx;
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
    private Vertx vertx;

    public PostApi(PostService postService, CommentService commentService, Vertx vertx) {
        this.postService = postService;
        this.commentService = commentService;
        this.vertx = vertx;
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

                // Notify new post
                JsonObject newComment = new JsonObject()
                        .put("type", "view-post")
                        .put("postId", post.getId())
                        .put("views", post.getViews() + 1);

                vertx.eventBus().send("sent-to-users", newComment);

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

        Post post;
        try {
            JsonObject bodyAsJson = context.getBodyAsJson();
            post = new Post(bodyAsJson);
        } catch (Exception ex) {
            context.response().setStatusCode(400)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("error", true).put("message", "Invalid Json").toString());
            return;
        }

        User user = context.get("user");
        post.setUserId(user.getId());

        final Post post1 = post;
        postService.addPost(post, res -> {
            if (res.succeeded()) {
                post1.setId(res.result());
                context.response().setStatusCode(201)
                        .putHeader("Content-Type", "application/json")
                        .end(post1.toJson().toBuffer());

                // Notify new post
                JsonObject newPost = new JsonObject()
                        .put("type", "new-post")
                        .put("userId", user.getId())
                        .put("userEmail", user.getEmail())
                        .put("post", post1.toJson());

                vertx.eventBus().send("sent-to-users", newPost);

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

                        // Notify new post
                        JsonObject newComment = new JsonObject()
                                .put("type", "new-comment")
                                .put("postId", post.getId())
                                .put("comments", post.getComments() + 1)
                                .put("commendId", comment.getId())
                                .put("commentBody", comment.getBody())
                                .put("userId", user.getId())
                                .put("userEmail", user.getEmail());

                        vertx.eventBus().send("sent-to-users", newComment);
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

                        // Notify new post
                        JsonObject newComment = new JsonObject()
                                .put("type", "likes")
                                .put("likeType", "like")
                                .put("postId", post.getId())
                                .put("postTitle", post.getTitle())
                                .put("likes", post.getLikes() + 1);

                        vertx.eventBus().send("sent-to-users", newComment);

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

                        if (post.getLikes() > 0) {
                            // Notify new post
                            JsonObject newComment = new JsonObject()
                                    .put("type", "likes")
                                    .put("likeType", "dislike")
                                    .put("postTitle", post.getTitle())
                                    .put("postId", post.getId())
                                    .put("likes", post.getLikes() - 1);

                            vertx.eventBus().send("sent-to-users", newComment);
                        }
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
