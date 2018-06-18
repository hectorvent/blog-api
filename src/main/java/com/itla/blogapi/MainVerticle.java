/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi;

import com.itla.blogapi.post.imp.CommentServiceImpl;
import com.itla.blogapi.user.UserApi;
import com.itla.blogapi.user.UserService;
import com.itla.blogapi.user.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import com.itla.blogapi.post.CommentService;
import com.itla.blogapi.post.PostApi;
import com.itla.blogapi.post.PostService;
import com.itla.blogapi.post.imp.PostServiceImpl;
import com.itla.blogapi.security.SecurityApi;
import com.itla.blogapi.security.SecurityApi2;
import com.itla.blogapi.security.UserTokenAuth;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hectorvent@gmail.com
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        JsonObject config = new JsonObject()
                // .put("url", "jdbc:mysql://localhost:3306/postdb?characterEncoding=UTF-8&useSSL=false")
                // .put("driver_class", "com.mysql.cj.jdbc.Driver")
                .put("url", "jdbc:sqlite:blogapi.db")
                .put("driver_class", "org.sqlite.JDBC")
                .put("user", "")
                .put("password", "");

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.PUT);
        allowMethods.add(HttpMethod.OPTIONS);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        PostService ps = new PostServiceImpl(vertx, config);
        CommentService cs = new CommentServiceImpl(vertx, config);
        UserService us = new UserServiceImpl(vertx, config);

        // public
        SecurityApi sa = new SecurityApi(us);
        sa.start(router);

        router.route().handler(UserTokenAuth.create(us));

        // private
        SecurityApi2 sa2 = new SecurityApi2(us);
        sa2.start(router);

        PostApi postApi = new PostApi(ps, cs);
        postApi.start(router);

        UserApi userApi = new UserApi(us);
        userApi.start(router);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, ar -> {
                    if (ar.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });
    }

}
