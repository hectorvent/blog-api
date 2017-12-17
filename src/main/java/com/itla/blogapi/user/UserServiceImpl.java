/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.user;

import com.itla.blogapi.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author hectorvent
 */
public class UserServiceImpl extends JdbcRepositoryWrapper implements UserService {

    public UserServiceImpl(Vertx vertx, JsonObject config) {
        super(vertx, config);
    }

    @Override
    public void addUser(User user, Handler<AsyncResult<Integer>> resulHandler) {

        JsonArray params = new JsonArray()
                .add(user.getName())
                .add(user.getEmail())
                .add(user.getPassword());

        insert(params, INSERT_STATEMANT, resulHandler);
    }

    @Override
    public void getUsers(Handler<AsyncResult<List<User>>> resulHandler) {
        this.retrieveMany(new JsonArray(), SELECT_ALL_STATEMENT)
                .map(rows -> rows.stream().map(User::new).map(u -> u.setPassword(null)).collect(Collectors.toList()))
                .setHandler(resulHandler);
    }

    @Override
    public void getUser(Integer id, Handler<AsyncResult<User>> resultHandler) {
        this.retrieveOne(id, SELECT_ALL_STATEMENT_ID)
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(resultHandler);
    }

    @Override
    public void login(User user, Handler<AsyncResult<String>> resultHandler) {

        this.retrieveOne(SELECT_ALL_STATEMENT_LOGIN, user.getEmail(), user.getPassword())
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(r -> {

                    if (r.succeeded()) {
                        User u = r.result();
                        String token = UUID.randomUUID().toString();

                        JsonArray params = new JsonArray().add(u.getId()).add(token);

                        SharedData sd = vertx().sharedData();

                        LocalMap<String, Integer> tokens = sd.getLocalMap("tokens");
                        tokens.put(token, u.getId());

                        this.executeNoResult(params, INSERT_STATEMANT_TOKEN, res -> {
                            if (res.succeeded()) {

                                resultHandler.handle(Future.succeededFuture(token));
                            } else {
                                resultHandler.handle(Future.failedFuture(res.cause()));
                            }
                        });

                    } else {
                        resultHandler.handle(Future.failedFuture(r.cause()));
                    }
                });
    }

    @Override
    public void logout(String token, Handler<AsyncResult<Void>> resultHandler) {

        SharedData sd = vertx().sharedData();
        LocalMap<String, Integer> tokens = sd.getLocalMap("tokens");
        tokens.remove(token);
        this.executeNoResult(new JsonArray().add(token), DELETE_STATEMANT_TOKEN, resultHandler);
    }

    private static final String INSERT_STATEMANT = "INSERT INTO user (name, email, password) VALUES (?,?,?)";

    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM user";
    private static final String SELECT_ALL_STATEMENT_ID = SELECT_ALL_STATEMENT + " WHERE id = ?";
    private static final String SELECT_ALL_STATEMENT_LOGIN = SELECT_ALL_STATEMENT + " WHERE email = ? AND password = ?";

    // token
    private static final String INSERT_STATEMANT_TOKEN = "INSERT INTO token (userId, token) VALUES (?,?)";
    private static final String DELETE_STATEMANT_TOKEN = "DELETE FROM token WHERE token = ?";
    private static final String SELETE_STATEMANT_TOKEN = "SELECT * FROM token";

    @Override
    public void getTokens(Handler<AsyncResult<List<Token>>> resultHandler) {
        this.retrieveMany(new JsonArray(), SELETE_STATEMANT_TOKEN)
                .map(rows -> rows.stream().map(Token::new).collect(Collectors.toList()))
                .setHandler(resultHandler);

    }

}
