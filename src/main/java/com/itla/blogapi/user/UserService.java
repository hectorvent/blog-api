package com.itla.blogapi.user;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;

/**
 *
 * @author hectorvent@gmail.com
 */
public interface UserService {

    public void addUser(User user, Handler<AsyncResult<Integer>> resulHandler);

    public void getUsers(Handler<AsyncResult<List<User>>> resulHandler);

    public void getUser(Integer id, Handler<AsyncResult<User>> resultHandler);

    public void login(User user, Handler<AsyncResult<String>> resultHandler);

    public void logout(String token, Handler<AsyncResult<Void>> resultHandler);

    public void getToken(String token, Handler<AsyncResult<User>> resultHandler);

}
