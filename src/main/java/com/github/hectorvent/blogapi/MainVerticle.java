package com.github.hectorvent.blogapi;

import com.github.hectorvent.blogapi.post.PostApi;
import com.github.hectorvent.blogapi.post.PostService;
import com.github.hectorvent.blogapi.post.imp.CommentServiceImpl;
import com.github.hectorvent.blogapi.post.imp.PostServiceImpl;
import com.github.hectorvent.blogapi.security.SecurityApi;
import com.github.hectorvent.blogapi.security.SecurityApi2;
import com.github.hectorvent.blogapi.security.UserTokenAuth;
import com.github.hectorvent.blogapi.user.User;
import com.github.hectorvent.blogapi.user.UserApi;
import com.github.hectorvent.blogapi.user.UserService;
import com.github.hectorvent.blogapi.user.UserServiceImpl;
import com.github.hectorvent.blogapi.utils.HttpUtils;
import com.github.hectorvent.blogapi.websocket.Client;
import com.github.hectorvent.blogapi.websocket.ConnectedClientStore;
import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.Set;

/**
 *
 * @author Hector Ventura <hectorvent@gmail.com>
 */
public class MainVerticle extends AbstractVerticle {

    private UserService userService;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        setupConfig();

        Handler handler = BodyHandler.create()
                .setBodyLimit(1024 * 1024L);

        Router router = Router.router(vertx);
        router.route().handler(handler);

        Set<String> allowHeaders = Set.of("x-requested-with",
                "Access-Control-Allow-Origin",
                "origin",
                "Content-Type",
                "accept",
                "Authorization");
        Set<HttpMethod> allowMethods = Set.of(HttpMethod.GET,
                HttpMethod.PUT,
                HttpMethod.OPTIONS,
                HttpMethod.POST,
                HttpMethod.DELETE,
                HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        PostService ps = new PostServiceImpl(vertx, config());
        CommentServiceImpl cs = new CommentServiceImpl(vertx, config());
        userService = new UserServiceImpl(vertx, config());

        // public
        SecurityApi sa = new SecurityApi(userService);
        sa.start(router);

        router.route().handler(UserTokenAuth.create(userService));

        // private
        SecurityApi2 sa2 = new SecurityApi2(userService);
        sa2.start(router);

        PostApi postApi = new PostApi(ps, cs, vertx);
        postApi.start(router);

        UserApi userApi = new UserApi(userService);
        userApi.start(router);

        vertx.createHttpServer()
                .webSocketHandler(createWebSocketHandler())
                .requestHandler(router)
                .listen(8080, ar -> {
                    if (ar.succeeded()) {
                        startPromise.complete();
                    } else {
                        startPromise.fail(ar.cause());
                    }
                });

        forewardMessageToUserConsumer();
    }

    public void setupConfig() {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        config().put("user", dotenv.get("DATABASE_USER", "root"))
                .put("password", dotenv.get("DATABASE_PASSWORD", "password"))
                .put("url", dotenv.get("DATABASE_URL", "jdbc:mysql://localhosy:3306/blogapi?characterEncoding=UTF-8"))
                .put("max_pool_size", 10)
                .put("max_idle_time", 10)
                .put("min_pool_size", 3)
                .put("driver_class", "com.mysql.jdbc.Driver");
    }

    private Handler<ServerWebSocket> createWebSocketHandler() {
        return socket -> {
            String tokenCode = HttpUtils.getToken(socket.query());
            Client client = new Client(socket.textHandlerID(), tokenCode);

            authenticateClient(client, res -> {
                if (res.succeeded()) {
                    socket.closeHandler(v -> {
                        ConnectedClientStore.get().remove(client);

                        User user = client.getUser();
                        JsonObject message = new JsonObject()
                                .put("type", "disconnected")
                                .put("userEmail", user.getEmail())
                                .put("userName", user.getName())
                                .put("userId", user.getId());

                        ConnectedClientStore.get()
                                .filterClients(pr -> true)
                                .forEach(c -> {
                                    vertx.eventBus().send(c.getSocketId(), message.encode());
                                });
                    });
                } else {
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

        if (!client.getTokenCode().isPresent()) {
            handler.handle(Future.failedFuture("Token is missing"));
            return;
        }

        userService.getToken(client.getTokenCode().get(), res -> {

            if (res.succeeded()) {
                User user = res.result();
                client.setUser(user);

                ConnectedClientStore.get().put(client);

                JsonObject message = new JsonObject()
                        .put("type", "logged")
                        .put("userEmail", user.getEmail())
                        .put("userName", user.getName())
                        .put("userId", user.getId());

                JsonArray users = new JsonArray();
                ConnectedClientStore.get()
                        .filterClients(pr -> !pr.getSocketId().equals(client.getSocketId()))
                        .stream()
                        .map(c -> {
                            JsonObject jo = new JsonObject()
                                    .put("userId", c.getUser().getId())
                                    .put("userEmail", c.getUser().getEmail());
                            return jo;
                        })
                        .forEach(users::add);

                message.put("users", users);

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
    }

}
