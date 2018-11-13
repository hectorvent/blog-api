package com.itla.blogapi.post.imp;

import com.itla.blogapi.post.Post;
import com.itla.blogapi.post.PostService;
import com.itla.blogapi.utils.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;
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

        JsonArray params = new JsonArray()
                .add(post.getTitle())
                .add(post.getBody())
                .add(post.getUserId());

        insert(params, INSERT_STATEMANT, resulHandler);
    }

    @Override
    public void getPosts(Map<String, String> params, Handler<AsyncResult<List<Post>>> resulHandler) {

        if (params.containsKey("userId")) {
            retrieveMany(new JsonArray().add(String.valueOf(params.get("userId"))), SELECT_ALL_STATEMENT_USERID)
                    .map(rows -> rows.stream().map(Post::new).collect(Collectors.toList()))
                    .setHandler(resulHandler);
        } else {

            retrieveMany(new JsonArray(), SELECT_ALL_STATEMENT)
                    .map(rows -> rows.stream().map(Post::new).collect(Collectors.toList()))
                    .setHandler(resulHandler);
        }
    }

    private static final String INSERT_STATEMANT = "INSERT INTO post (title, body, userId) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM post";
    private static final String SELECT_ALL_STATEMENT_ID = SELECT_ALL_STATEMENT + " WHERE id = ?";
    private static final String SELECT_ALL_STATEMENT_USERID = SELECT_ALL_STATEMENT + " WHERE userId = ?";

    @Override
    public void getPost(Integer id, Handler<AsyncResult<Post>> resultHandler) {
        this.retrieveOne(SELECT_ALL_STATEMENT_ID, id)
                .map(option -> option.map(Post::new).orElse(null))
                .setHandler(resultHandler);
    }
}
