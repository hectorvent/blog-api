package com.itla.blogapi.security;

import com.itla.blogapi.user.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author hectorvent@gmail.com
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

        userService.logout(user.getToken().getToken(), res -> {
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
