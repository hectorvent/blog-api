package com.itla.blogapi.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author hectorvent@gmail.com
 */
public class JdbcRepositoryWrapper {

    protected final JDBCClient client;
    private final Vertx vertx;

    public JdbcRepositoryWrapper(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.client = JDBCClient.createNonShared(vertx, config);
    }

    protected void execute(String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.execute(sql, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    /**
     * Suitable for `add`, `exists` operation.
     *
     * @param params query params
     * @param sql sql
     * @param resultHandler async result handler
     */
    protected void executeNoResult(JsonArray params, String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.updateWithParams(sql, params, r -> {
                if (r.succeeded()) {

                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    /**
     * Suitable for `add`, `exists` operation.
     *
     * @param params query params
     * @param sql sql
     * @param resultHandler async result handler
     */
    protected void insert(JsonArray params, String sql, Handler<AsyncResult<Integer>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.updateWithParams(sql, params, r -> {
                if (r.succeeded()) {

                    Integer id = r.result().getKeys().getInteger(0);
                    resultHandler.handle(Future.succeededFuture(id));
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    protected <R> void execute(JsonArray params, String sql, R ret, Handler<AsyncResult<R>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.updateWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(ret));
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

//    protected <K> Future<Optional<JsonObject>> retrieveOne(K param, String sql) {
//        return getConnection()
//                .compose(connection -> {
//                    Future<Optional<JsonObject>> future = Future.future();
//                    connection.queryWithParams(sql, new JsonArray().add(param), r -> {
//                        if (r.succeeded()) {
//                            List<JsonObject> resList = r.result().getRows();
//                            if (resList == null || resList.isEmpty()) {
//                                future.fail("Not found");
//                            } else {
//                                future.complete(Optional.of(resList.get(0)));
//                            }
//                        } else {
//                            future.fail(r.cause());
//                        }
//                        connection.close();
//                    });
//                    return future;
//                });
//    }

    protected <K> Future<Optional<JsonObject>> retrieveOne(String sql, K... param) {
        return getConnection()
                .compose(connection -> {
                    Future<Optional<JsonObject>> future = Future.future();

                    JsonArray jsonArray = new JsonArray();
                    for (K k : param) {
                        jsonArray.add(k);
                    }

                    connection.queryWithParams(sql, jsonArray, r -> {
                        if (r.succeeded()) {
                            List<JsonObject> resList = r.result().getRows();
                            if (resList == null || resList.isEmpty()) {
                                future.fail("Not found");
                            } else {
                                future.complete(Optional.of(resList.get(0)));
                            }
                        } else {
                            future.fail(r.cause());
                        }
                        connection.close();
                    });
                    return future;
                });
    }

    protected int calcPage(int page, int limit) {
        if (page <= 0) {
            return 0;
        }
        return limit * (page - 1);
    }

    protected Future<List<JsonObject>> retrieveByPage(int page, int limit, String sql) {
        JsonArray params = new JsonArray().add(calcPage(page, limit)).add(limit);
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.queryWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
                connection.close();
            });
            return future;
        });
    }

    protected Future<List<JsonObject>> retrieveMany(JsonArray param, String sql) {
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();
            connection.queryWithParams(sql, param, r -> {
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
                connection.close();
            });
            return future;
        });
    }

    protected Future<List<JsonObject>> retrieveAll(String sql) {
        return getConnection().compose(connection -> {
            Future<List<JsonObject>> future = Future.future();

            connection.query(sql, r -> {
                if (r.succeeded()) {
                    future.complete(r.result().getRows());
                } else {
                    future.fail(r.cause());
                }
                connection.close();
            });
            return future;
        });
    }

    protected <K> void removeOne(K id, String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            JsonArray params = new JsonArray().add(id);
            connection.queryWithParams(sql, params, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    protected void removeAll(String sql, Handler<AsyncResult<Void>> resultHandler) {
        client.getConnection(connHandler(resultHandler, connection -> {
            connection.update(sql, r -> {
                if (r.succeeded()) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture(r.cause()));
                }
                connection.close();
            });
        }));
    }

    /**
     * A helper methods that generates async handler for SQLConnection
     *
     * @param <R>
     * @param h1
     * @param h2
     * @return generated handler
     */
    protected <R> Handler<AsyncResult<SQLConnection>> connHandler(Handler<AsyncResult<R>> h1, Handler<SQLConnection> h2) {
        return conn -> {
            if (conn.succeeded()) {
                final SQLConnection connection = conn.result();
                h2.handle(connection);
            } else {
                h1.handle(Future.failedFuture(conn.cause()));
            }
        };
    }

    protected Future<SQLConnection> getConnection() {
        Future<SQLConnection> future = Future.future();
        client.getConnection(future.completer());
        return future;
    }

    protected String emptyIfNull(String object) {
        if (object == null) {
            return "";
        }

        return object;
    }

    
    protected Vertx vertx(){
        return this.vertx;
    }
}
