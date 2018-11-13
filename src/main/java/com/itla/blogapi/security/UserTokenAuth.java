package com.itla.blogapi.security;

import com.itla.blogapi.security.impl.UserTokenAuthImpl;
import com.itla.blogapi.user.UserService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author hectorvent@gmail.com
 */
public interface UserTokenAuth extends Handler<RoutingContext> {

    static UserTokenAuth create(UserService userService) {
        return new UserTokenAuthImpl(userService);
    }

}
