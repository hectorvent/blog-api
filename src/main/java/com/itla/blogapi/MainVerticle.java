/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itla.blogapi;

import com.itla.blogapi.comment.CommentApi;
import com.itla.blogapi.comment.CommentServiceImpl;
import com.itla.blogapi.user.Token;
import com.itla.blogapi.user.UserApi;
import com.itla.blogapi.user.UserService;
import com.itla.blogapi.user.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.List;
import com.itla.blogapi.comment.CommentService;
import com.itla.blogapi.post.PostApi;
import com.itla.blogapi.post.PostService;
import com.itla.blogapi.post.PostServiceImpl;
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

        SharedData sd = vertx.sharedData();
        LocalMap<String, Integer> tokens = sd.getLocalMap("tokens");

        JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://localhost:3306/postdb?characterEncoding=UTF-8&useSSL=false")
                .put("driver_class", "com.mysql.cj.jdbc.Driver")
                .put("user", "root")
                .put("password", "quisquella")
                .put("max_pool_size", 30);

        PostService ps = new PostServiceImpl(vertx, config);
        CommentService cs = new CommentServiceImpl(vertx, config);
        UserService us = new UserServiceImpl(vertx, config);

        us.getTokens(rs -> {

            if (rs.succeeded()) {
                List<Token> dbTokens = rs.result();
                System.out.println("Sessiones activas = " + dbTokens.size());
                for (Token dbToken : dbTokens) {
                    tokens.put(dbToken.getToken(), dbToken.getUserId());
                }

            } else {
                System.out.println(rs.cause().getMessage());
            }

        });

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

        CommentApi commentApi = new CommentApi(cs, tokens);
        commentApi.start(router);

        PostApi postApi = new PostApi(ps, tokens);
        postApi.start(router);

        UserApi userApi = new UserApi(us, tokens);
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

//        Post post = new Post();
//        post.setBody("Probando el post2");
//        post.setTitle("Clase de Javascript superado");
//        post.setUserId(1);
//
//        vertx.setPeriodic(1000, h -> {
//
//            System.out.println(tokens.size());
//        });
    }

}
