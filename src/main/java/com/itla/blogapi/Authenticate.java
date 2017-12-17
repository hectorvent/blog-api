/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author hectorvent@gmail.com
 */
public class Authenticate {

    LocalMap<String, Integer> tokens;

    public Authenticate(LocalMap<String, Integer> tokens) {
        this.tokens = tokens;
    }

    public void authenticate(RoutingContext context, Handler<AsyncResult<Integer>> result) {

        String token = context.request().params().get("token");

        Integer id = tokens.get(token == null ? "" : token);

        if (id == null) {
            context.response().setStatusCode(304)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("estado", "error").put("login", "requerido").encode());
        } else {
            result.handle(Future.succeededFuture(id));
        }

    }

}
