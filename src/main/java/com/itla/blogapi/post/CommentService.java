package com.itla.blogapi.post;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;

/**
 *
 * @author Hector Ventura <hectorvent@gmail.com>
 */
public interface CommentService {

    public void addComment(Comment post, Handler<AsyncResult<Integer>> resulHandler);

    public void getComments(Integer id, Handler<AsyncResult<List<Comment>>> resultHandler);

}
