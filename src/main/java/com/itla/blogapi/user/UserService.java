/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.user;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;

/**
 *
 * @author hectorvent
 */
public interface UserService {

    public void addUser(User user, Handler<AsyncResult<Integer>> resulHandler);

    public void getUsers(Handler<AsyncResult<List<User>>> resulHandler);

    public void getUser(Integer id, Handler<AsyncResult<User>> resultHandler);

    public void login(User user, Handler<AsyncResult<String>> resultHandler);

    public void logout(String token, Handler<AsyncResult<Void>> resultHandler);

    public void getTokens(Handler<AsyncResult<List<Token>> > resultHandler);

}
