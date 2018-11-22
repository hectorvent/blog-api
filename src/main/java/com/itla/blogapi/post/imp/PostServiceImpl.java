package com.itla.blogapi.post.imp;

import com.itla.blogapi.post.Post;
import com.itla.blogapi.post.PostService;
import com.itla.blogapi.user.User;
import com.itla.blogapi.utils.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author hectorvent@gmail.com
 */
public class PostServiceImpl extends JdbcRepositoryWrapper implements PostService {

    public PostServiceImpl(Vertx vertx, JsonObject config) {
        super(vertx, config);
    }

    @Override
    public void addPost(Post post, Handler<AsyncResult<Integer>> resulHandler) {

        long createdAt = new Date().getTime();
        post.setCreatedAt(createdAt);

        StringBuilder tags = new StringBuilder("");

        post.getTags().stream().forEach(t -> {
            if (tags.length() > 0) {
                tags.append(",");
            }
            tags.append(t);
        });

        JsonArray params = new JsonArray()
                .add(post.getTitle())
                .add(post.getBody())
                .add(post.getUserId())
                .add(post.getCreatedAt())
                .add(tags);

        insert(params, INSERT_STATEMANT, resulHandler);
    }

    Function<JsonObject, Post> postTransform = (o) -> {

        Post p = new Post(o);

        if (o.getValue("liked") instanceof Number) {
            p.setLiked(o.getInteger("liked") > 0);
        }

        String tags = o.getString("tags");
        String t[] = tags.split(",");
        Set<String> ts = new HashSet();
        p.setTags(ts);
        if (t.length > 0 && tags.length() > 1) {
            ts.addAll(Arrays.asList(t));
        }

        return p;
    };

    @Override
    public void getPosts(User user, Map<String, String> params, Handler<AsyncResult<List<Post>>> resulHandler) {

        if (params.containsKey("userId")) {
            retrieveMany(new JsonArray().add(user.getId()).add(String.valueOf(params.get("userId"))), SELECT_ALL_STATEMENT_USERID)
                    .map(rows -> rows.stream().map(postTransform).collect(Collectors.toList()))
                    .setHandler(resulHandler);
        } else {
            retrieveMany(new JsonArray().add(user.getId()), SELECT_ALL_STATEMENT)
                    .map(rows -> rows.stream().map(postTransform).collect(Collectors.toList()))
                    .setHandler(resulHandler);
        }
    }

    @Override
    public void getPost(User user, Integer id, Handler<AsyncResult<Post>> resultHandler) {
        this.retrieveOne(SELECT_ALL_STATEMENT_ID, user.getId(), id)
                .map(option -> option.map(postTransform).orElse(null))
                .setHandler(resultHandler);
    }

    @Override
    public void incremetView(Post post, Handler<AsyncResult<Void>> resultHandler) {
        executeNoResult(new JsonArray().add(post.getId()), UPDATE_POST_VIEWS, resultHandler);
    }

    @Override
    public void addLike(User user, Post post, Handler<AsyncResult<Void>> resultHandler) {
        JsonArray data = new JsonArray().add(post.getId()).add(user.getId());
        executeNoResult(data, INSERT_POST_LIKE, resultHandler);
    }

    @Override
    public void removeLike(User user, Post post, Handler<AsyncResult<Void>> resultHandler) {
        executeNoResult(new JsonArray().add(post.getId()).add(user.getId()), REMOVE_POST_LIKE, resultHandler);
    }

    private static final String INSERT_STATEMANT = "INSERT INTO post (title, body, userId, createdAt, tags) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_STATEMENT = "SELECT post.id, title, body, userId, post.createdAt, "
            + "tags, views, name AS userName, email AS userEmail, "
            + "(SELECT count(postId) FROM post_like WHERE postId = post.id) as likes, "
            + "(SELECT if(count(postId)>0,true, false) FROM post_like WHERE userId = ? AND postId = post.id) AS liked "
            + "FROM post INNER JOIN user on (post.userId = user.id)";
    private static final String SELECT_ALL_STATEMENT_ID = SELECT_ALL_STATEMENT + " WHERE post.id = ?";
    private static final String SELECT_ALL_STATEMENT_USERID = SELECT_ALL_STATEMENT + " WHERE userId = ?";
    private static final String UPDATE_POST_VIEWS = "UPDATE post SET views = views+1 WHERE id = ?";
    private static final String INSERT_POST_LIKE = "INSERT INTO post_like (postId, userId) VALUES (?, ?)";
    private static final String REMOVE_POST_LIKE = "DELETE FROM post_like WHERE postId = ? AND userId = ?";

}
