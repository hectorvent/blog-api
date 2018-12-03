package com.itla.blogapi;

import com.itla.blogapi.post.CommentService;
import com.itla.blogapi.post.PostApi;
import com.itla.blogapi.post.PostService;
import com.itla.blogapi.post.imp.CommentServiceImpl;
import com.itla.blogapi.post.imp.PostServiceImpl;
import com.itla.blogapi.security.SecurityApi;
import com.itla.blogapi.security.SecurityApi2;
import com.itla.blogapi.security.UserTokenAuth;
import com.itla.blogapi.user.User;
import com.itla.blogapi.user.UserApi;
import com.itla.blogapi.user.UserService;
import com.itla.blogapi.user.UserServiceImpl;
import com.itla.blogapi.utils.HttpUtils;
import com.itla.blogapi.websocket.Client;
import com.itla.blogapi.websocket.ConnectedClientStore;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hectorvent@gmail.com
 */
public class MainVerticle extends AbstractVerticle {

    UserService us;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        System.out.println("Version : 1.3");
        setupConfig();
        System.out.println(config());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        allowHeaders.add("Authorization");
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

        PostService ps = new PostServiceImpl(vertx, config());
        CommentService cs = new CommentServiceImpl(vertx, config());
        us = new UserServiceImpl(vertx, config());

        // public
        SecurityApi sa = new SecurityApi(us);
        sa.start(router);

        router.route().handler(UserTokenAuth.create(us));

        // private
        SecurityApi2 sa2 = new SecurityApi2(us);
        sa2.start(router);

        PostApi postApi = new PostApi(ps, cs, vertx);
        postApi.start(router);

        UserApi userApi = new UserApi(us);
        userApi.start(router);

        vertx.createHttpServer()
                .websocketHandler(createWebSocketHandler())
                .requestHandler(router::accept)
                .listen(8080, ar -> {
                    if (ar.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });

        forewardMessageToUserConsumer();
    }

    public void setupConfig() {

        Map<String, String> env = System.getenv();

        if (env.containsKey("DATABASE_USER")) {
            config().put("user", env.get("DATABASE_USER"));
        }

        if (env.containsKey("DATABASE_PASSWORD")) {
            config().put("password", env.get("DATABASE_PASSWORD"));
        }

        if (env.containsKey("DATABASE_URL")) {
            config().put("url", env.get("DATABASE_URL"));
        }

        config().put("max_pool_size", 10)
                .put("max_idle_time", 10)
                .put("min_pool_size", 3)
                .put("driver_class", "com.mysql.jdbc.Driver");
    }

    private Handler<ServerWebSocket> createWebSocketHandler() {
        return socket -> {
            String tokenCode = HttpUtils.getToken(socket.query());
            Client client = new Client(socket.textHandlerID(), tokenCode);
//            LOG.debug("Socket Client connecting with tokenCode = {}", tokenCode);

            authenticateClient(client, res -> {

                if (res.succeeded()) {
                    System.out.println("Socket Client connected with tokenCode = " + tokenCode);
                    socket.closeHandler(v -> {
//                        LOG.info("The user has gone off");
                        ConnectedClientStore.get().remove(client);

                        // publish connected users.
                    });
                } else {
//                    LOG.debug("Error login - {}", res.cause().getMessage());
                    JsonObject message = new JsonObject()
                            .put("type", "error")
                            .put("error", "BadToken")
                            .put("message", "Bad auth token");
                    socket.writeTextMessage(message.encode());
                    socket.close();
                }
            });
        };
    }

    private void forewardMessageToUserConsumer() {

        vertx.eventBus().<JsonObject>consumer("sent-to-users", (e) -> {
            ConnectedClientStore.get()
                    .filterClients(pr -> true)
                    .forEach(c -> {
                        vertx.eventBus().send(c.getSocketId(), e.body().encode());
                    });
        });

    }

    private void authenticateClient(Client client, Handler<AsyncResult<Void>> handler) {

        if (client.getTokenCode().isPresent()) {

            us.getToken(client.getTokenCode().get(), res -> {

                if (res.succeeded()) {
                    User user = res.result();
                    client.setUser(user);

                    ConnectedClientStore.get().put(client);

                    JsonObject message = new JsonObject()
                            .put("type", "logged")
                            .put("userEmail", user.getEmail())
                            .put("userId", user.getId());

                    vertx.eventBus().send(client.getSocketId(), message.encode());

                    JsonObject newLogin = new JsonObject()
                            .put("type", "user-connected")
                            .put("userEmail", user.getEmail())
                            .put("userId", user.getId());

                    ConnectedClientStore.get()
                            .filterClients(pr -> !pr.getSocketId().equals(client.getSocketId()))
                            .forEach(c -> {
                                vertx.eventBus().send(c.getSocketId(), newLogin.encode());
                            });
                    handler.handle(Future.succeededFuture());
                } else {

                    handler.handle(Future.failedFuture(res.cause().getMessage()));
                }

            });
        } else {
            handler.handle(Future.failedFuture("Token is missing"));
        }
    }

}
