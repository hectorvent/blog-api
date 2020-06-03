package com.github.hectorvent.blogapi.security;

import com.github.hectorvent.blogapi.security.impl.UserTokenAuthImpl;
import com.github.hectorvent.blogapi.user.UserService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author Hector Ventura <hectorvent@gmail.com>
 */
public interface UserTokenAuth extends Handler<RoutingContext> {

    static UserTokenAuth create(UserService userService) {
        return new UserTokenAuthImpl(userService);
    }

}
