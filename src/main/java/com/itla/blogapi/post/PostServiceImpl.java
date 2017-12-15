/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.post;

import com.itla.blogapi.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;

/**
 *
 * @author hectorvent
 */
public class PostServiceImpl extends JdbcRepositoryWrapper implements PostService {

    public PostServiceImpl(Vertx vertx, JsonObject config) {
        super(vertx, config);
    }

    @Override
    public void addPost(Post post, Handler<AsyncResult<Void>> resulHandler) {

        String sql = "CREATE TABLE IF NO EXISTS usuario ( name text)";

        execute(sql, resulHandler);

    }

    @Override
    public void getPosts(Handler<AsyncResult<List<Post>>> resulHandler) {

    }

}
