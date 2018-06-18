/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.post;

import com.itla.blogapi.post.Comment;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;

/**
 *
 * @author hectorvent
 */
public interface CommentService {

    public void addComment(Comment post, Handler<AsyncResult<Integer>> resulHandler);

    public void getComments(Integer id, Handler<AsyncResult<List<Comment>>> resultHandler);

}
