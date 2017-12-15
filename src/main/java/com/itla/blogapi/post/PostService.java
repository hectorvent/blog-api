/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.post;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;

/**
 *
 * @author hectorvent
 */
public interface PostService {

    public void addPost(Post post, Handler<AsyncResult<Void>> resulHandler);

    public void getPosts(Handler<AsyncResult<List<Post>>> resulHandler);

}
