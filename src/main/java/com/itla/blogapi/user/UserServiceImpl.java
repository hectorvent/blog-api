package com.itla.blogapi.user;

import com.itla.blogapi.utils.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author hectorvent@gmail.com
 */
public class UserServiceImpl extends JdbcRepositoryWrapper implements UserService {

    public UserServiceImpl(Vertx vertx, JsonObject config) {
        super(vertx, config);
    }

    @Override
    public void addUser(User user, Handler<AsyncResult<Integer>> resulHandler) {

        long createdAd = new Date().getTime();
        JsonArray params = new JsonArray()
                .add(user.getName())
                .add(user.getEmail())
                .add(user.getPassword())
                .add(createdAd);

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
        this.retrieveOne(SELECT_ALL_STATEMENT_ID, id)
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(resultHandler);
    }

    @Override
    public void login(User user, Handler<AsyncResult<User>> resultHandler) {

        retrieveOne(SELECT_ALL_STATEMENT_LOGIN, user.getEmail(), user.getPassword())
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(r -> {

                    if (r.succeeded()) {
                        User u = r.result();
                        String code = UUID.randomUUID().toString();
                        long createdAt = new Date().getTime();

                        Token token = new Token();
                        token.setToken(code);
                        token.setCreatedAt(createdAt);
                        token.setDescription("------");

                        JsonArray params = new JsonArray()
                                .add(u.getId())
                                .add(token.getToken())
                                .add(token.getCreatedAt())
                                .add(token.getDescription());

                        this.executeNoResult(params, INSERT_STATEMANT_TOKEN, res -> {
                            if (res.succeeded()) {
                                u.setToken(token);
                                resultHandler.handle(Future.succeededFuture(u));
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
        executeNoResult(new JsonArray().add(token), DELETE_STATEMANT_TOKEN, resultHandler);
    }

    @Override
    public void getToken(String token, Handler<AsyncResult<User>> resultHandler) {
        retrieveOne(SELECT_STATEMENT, token)
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(r -> {
                    if (r.succeeded()) {
                        User u = r.result();
                        u.setToken(new Token().setToken(token));
                        resultHandler.handle(Future.succeededFuture(u));
                    } else {
                        resultHandler.handle(Future.failedFuture(r.cause()));
                    }
                });
    }

    private static final String INSERT_STATEMANT = "INSERT INTO user (name, email, password, createdAt) VALUES (?,?,?,?)";

    private static final String SELECT_ALL_STATEMENT = "SELECT user.*,(SELECT count(userId) FROM post WHERE userId = user.id) AS posts FROM user";
    private static final String SELECT_ALL_STATEMENT_ID = SELECT_ALL_STATEMENT + " WHERE user.id = ?";
    private static final String SELECT_ALL_STATEMENT_LOGIN = SELECT_ALL_STATEMENT + " WHERE email = ? AND password = ?";

    // token
    private static final String INSERT_STATEMANT_TOKEN = "INSERT INTO token (userId, token, createdAt, description) VALUES (?, ?, ?, ?)";
    private static final String DELETE_STATEMANT_TOKEN = "DELETE FROM token WHERE token = ?";
    private static final String SELECT_STATEMENT = "SELECT u.*, (SELECT count(userId) FROM post WHERE userId = u.id) AS posts FROM user u INNER JOIN token t ON u.id = t.userId AND t.token = ?";

}
