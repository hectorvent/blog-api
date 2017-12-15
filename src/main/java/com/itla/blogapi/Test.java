/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 *
 * @author hectorvent
 */
public class Test extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        JsonObject config = new JsonObject()
                .put("url", "jdbc:sqlite:prueba.db")
                .put("driver_class", "org.sqlite.jdbcDriver")
                .put("max_pool_size", 30);

        JDBCClient client;
        SQLClient client = JDBCClient.createShared(vertx, config);

    }

}
