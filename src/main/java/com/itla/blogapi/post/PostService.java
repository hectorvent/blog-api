package com.itla.blogapi.post;

import com.itla.blogapi.user.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hectorvent@gmail.com
 */
public interface PostService {

    public void addPost(Post post, Handler<AsyncResult<Integer>> resulHandler);

    public void getPosts(User user, Map<String, String> params, Handler<AsyncResult<List<Post>>> resulHandler);

    public void getPost(User user, Integer id, Handler<AsyncResult<Post>> resultHandler);

    public void incremetView(Post post, Handler<AsyncResult<Void>> resultHandler);

    public void addLike(User user, Post post, Handler<AsyncResult<Void>> resultHandler);

    public void removeLike(User user, Post post, Handler<AsyncResult<Void>> resultHandler);

}
