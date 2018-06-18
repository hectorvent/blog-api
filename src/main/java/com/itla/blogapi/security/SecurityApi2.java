/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.security;

import com.itla.blogapi.user.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author hectorvent
 */
public class SecurityApi2 {

    private final static String LOGOUT_PATH = "/logout";
    private final UserService userService;

    public SecurityApi2(UserService userService) {
        this.userService = userService;
    }

    public void start(Router router) throws Exception {
        router.delete(LOGOUT_PATH).handler(this::logout);
    }

    private void logout(RoutingContext context) {

        User user = context.get("user");
        String token = context.get("token");
        System.out.println("TOKEN : " + token);
        userService.logout(token, res -> {
            if (res.succeeded()) {

                context.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("estatus", "ok").encode());
            } else {
                context.response().setStatusCode(403)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("estatus", "error").put("message", res.cause().getMessage()).toString());
            }

        });

    }

}
