/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.comment;

import com.itla.blogapi.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hectorvent
 */
public class CommentServiceImpl extends JdbcRepositoryWrapper implements CommentService {

    public CommentServiceImpl(Vertx vertx, JsonObject config) {
        super(vertx, config);
    }

    @Override
    public void addComment(Comment comment, Handler<AsyncResult<Integer>> resulHandler) {

        JsonArray params = new JsonArray()
                .add(comment.getPostId())
                .add(comment.getEmail())
                .add(comment.getName())
                .add(comment.getBody());

        insert(params, INSERT_STATEMANT, resulHandler);
    }

    @Override
    public void getComments(Integer postId, Handler<AsyncResult<List<Comment>>> resulHandler) {

        if (postId > 0) {
            this.retrieveMany(new JsonArray().add(postId), SELECT_ALL_STATEMENT_ID)
                    .map(rows -> rows.stream().map(Comment::new).collect(Collectors.toList()))
                    .setHandler(resulHandler);
        } else {

            this.retrieveMany(new JsonArray(), SELECT_ALL_STATEMENT)
                    .map(rows -> rows.stream().map(Comment::new).collect(Collectors.toList()))
                    .setHandler(resulHandler);
        }
    }

    private static final String INSERT_STATEMANT = "INSERT INTO comment (postId, email, name, body) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM comment";
    private static final String SELECT_ALL_STATEMENT_ID = SELECT_ALL_STATEMENT + " WHERE postId = ?";

   
}
