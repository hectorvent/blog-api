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
public class SecurityApi {

    private final static String LOGIN_PATH = "/login";
    private final static String REGISTER_PATH = "/register";
    private final UserService userService;

    public SecurityApi(UserService userService) {
        this.userService = userService;
    }

    public void start(Router router) throws Exception {

        router.post(REGISTER_PATH).handler(this::addUser);
        router.post(LOGIN_PATH).handler(this::login);
    }

    private void addUser(RoutingContext context) {

        User user = new User(context.getBodyAsJson());

        userService.addUser(user, res -> {
            if (res.succeeded()) {
                user.setId(res.result());
                context.response().setStatusCode(201)
                        .putHeader("Content-Type", "application/json")
                        .end(user.toJson().toString());
            } else {
                context.response().setStatusCode(400)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
            }
        });

    }

    private void login(RoutingContext context) {

        User user = new User(context.getBodyAsJson());

        userService.login(user, res -> {
            if (res.succeeded()) {

                context.response().setStatusCode(201)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("estatus", "ok").put("token", res.result()).encode());
            } else {
                context.response().setStatusCode(400)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("estatus", "error").put("message", res.cause().getMessage()).toString());
            }
        });

    }

}
