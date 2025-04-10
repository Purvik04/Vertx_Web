package org.example.authentication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class JwtAuthDemo extends AbstractVerticle {

    @Override
    public void start()
    {
        Router router = Router.router(vertx);

        // JWT config
        JWTAuthOptions config = new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setPath("keystore.jceks")
                        .setType("JCEKS")
                        .setPassword("secret"));

        JWTAuth jwtAuth = JWTAuth.create(vertx, config);

        router.route("/protected/*")
                .handler(JWTAuthHandler.create(jwtAuth))
                .failureHandler(routingContext -> {
                    routingContext
                            .response()
                            .setStatusCode(routingContext.statusCode())
                            .end("Please login again");
                });

        // Simulate login
        router.post("/login").handler(ctx -> {

            String username = ctx.request().getParam("username");

            String password = ctx.request().getParam("password");

            if (username == null || password == null) {
                ctx.response().setStatusCode(400).end("Username and password required");
                return;
            }

            // Simulate user authentication
            if ("purvik".equals(username) && "mind@123".equals(password))
            {
                String token = jwtAuth.generateToken(
                        new JsonObject().put("sub", username),
                        new JWTOptions().setExpiresInMinutes(30));

                ctx.response().end(token);
            }
            else
            {
                ctx.response().setStatusCode(401).end("Invalid credentials");
            }
        });

        // Protected route
        router.get("/protected/data").handler(ctx -> {

            String username = ctx.user().principal().getString("sub");

            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("message", "Hello " + username).toBuffer());
        });

        vertx.createHttpServer().requestHandler(router).listen(8888, res -> {
            if (res.succeeded())
            {
                System.out.println("JWT Server running at http://localhost:8888");
            }
            else
            {
                res.cause().printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new JwtAuthDemo());
    }
}

