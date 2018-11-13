package com.itla.blogapi.user;

import com.itla.blogapi.utils.JdbcRepositoryWrapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
        this.retrieveOne(SELECT_ALL_STATEMENT_ID, id)
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(resultHandler);
    }

    @Override
    public void login(User user, Handler<AsyncResult<String>> resultHandler) {

        retrieveOne(SELECT_ALL_STATEMENT_LOGIN, user.getEmail(), user.getPassword())
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(r -> {

                    if (r.succeeded()) {
                        User u = r.result();
                        String token = UUID.randomUUID().toString();

                        JsonArray params = new JsonArray()
                                .add(u.getId())
                                .add(token);

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
        executeNoResult(new JsonArray().add(token), DELETE_STATEMANT_TOKEN, resultHandler);
    }

    private static final String INSERT_STATEMANT = "INSERT INTO user (name, email, password) VALUES (?,?,?)";

    private static final String SELECT_ALL_STATEMENT = "SELECT * FROM user";
    private static final String SELECT_ALL_STATEMENT_ID = SELECT_ALL_STATEMENT + " WHERE id = ?";
    private static final String SELECT_ALL_STATEMENT_LOGIN = SELECT_ALL_STATEMENT + " WHERE email = ? AND password = ?";

    // token
    private static final String INSERT_STATEMANT_TOKEN = "INSERT INTO token (userId, token) VALUES (?,?)";
    private static final String DELETE_STATEMANT_TOKEN = "DELETE FROM token WHERE token = ?";
    private static final String SELETE_STATEMANT_TOKEN = "SELECT u.* FROM user u INNER JOIN token t ON u.id = t.userId AND t.token = ?";

    @Override
    public void getToken(String token, Handler<AsyncResult<User>> resultHandler) {
        retrieveOne(SELETE_STATEMANT_TOKEN, token)
                .map(option -> option.map(User::new).orElse(null))
                .setHandler(r -> {
                    if (r.succeeded()) {
                        User u = r.result();
                        resultHandler.handle(Future.succeededFuture(u));
                    } else {
                        resultHandler.handle(Future.failedFuture(r.cause()));
                    }
                });
    }

}
