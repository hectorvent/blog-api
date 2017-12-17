/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi.user;

import com.itla.blogapi.Authenticate;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author hectorvent
 */
public class UserApi extends Authenticate {

    private final static String PATH = "/users";
    private final static String PATH_ID = PATH + "/:userId";
    private final static String LOGIN_PATH = "/login";
    private final static String LOGOUT_PATH = "/logout";
    private final UserService userService;

    public UserApi(UserService userService, LocalMap<String, Integer> tokens) {
        super(tokens);
        this.userService = userService;
    }

    public void start(Router router) throws Exception {
        router.get(PATH).handler(this::getUsers);
        router.get(PATH_ID).handler(this::getUser);
        router.post(PATH).handler(this::addUser);
        router.post(LOGIN_PATH).handler(this::login);
        router.post(LOGOUT_PATH).handler(this::logout);
    }

    private void getUser(RoutingContext context) {

        authenticate(context, r -> {
            String userId = context.request().params().get("userId");

            userService.getUser(Integer.valueOf(userId), res -> {
                if (res.succeeded()) {
                    User user = res.result();
                    context.response().setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(user.toJson().encode());
                } else {
                    context.response().setStatusCode(404)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).toString());
                }
            });
        });
    }

    private void getUsers(RoutingContext context) {

        authenticate(context, r -> {
            userService.getUsers(res -> {
                if (res.succeeded()) {
                    JsonArray users = new JsonArray();
                    res.result().stream().map(User::toJson).forEach(users::add);
                    context.response().setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(users.encode());
                } else {
                    context.response().setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("error", true).put("message", res.cause().getMessage()).encode());
                }
            });
        });

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

    private void logout(RoutingContext context) {

        String token = context.getBodyAsJson().getString("token");

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
