/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.post;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hectorvent
 */
public interface PostService {

    public void addPost(Post post, Handler<AsyncResult<Integer>> resulHandler);

    public void getPosts(Map<String,String> params, Handler<AsyncResult<List<Post>>> resulHandler);
    
    public void getPost(Integer id, Handler<AsyncResult<Post>> resultHandler);

}
