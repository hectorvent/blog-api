package com.itla.blogapi.security.impl;

import com.itla.blogapi.user.User;
import com.itla.blogapi.user.UserService;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import com.itla.blogapi.security.UserTokenAuth;

/**
 *
 * @author hectorvent@gmail.com
 */
public class UserTokenAuthImpl implements UserTokenAuth {

    private final UserService userService;
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public UserTokenAuthImpl(UserService appService) {
        this.userService = appService;
    }

    @Override
    public void handle(RoutingContext context) {

        MultiMap headers = context.request().headers();

        if (!headers.contains(HEADER_AUTHORIZATION)) {
            notAuthorized(context);
            return;
        }

        String autherization = headers.get(HEADER_AUTHORIZATION);
        String parts[] = autherization.split(" ");

        if (parts.length != 2) {
            notAuthorized(context);
            return;
        }

        userService.getToken(parts[1], res -> {

            if (res.succeeded()) {
                User user = res.result();
                context.put("user", user);
                context.put("token", parts[1]);
                context.next();
            } else {
                notAuthorized(context);
            }

        });

    }

    private void notAuthorized(RoutingContext context) {
        context.response().setStatusCode(401)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject().put("estado", "error").put("login", "requerido").encode());
    }

}
