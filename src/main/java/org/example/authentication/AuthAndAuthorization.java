package org.example.authentication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class AuthAndAuthorization extends AbstractVerticle {

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

        router.get("/protected/*")
                .handler(JWTAuthHandler.create(jwtAuth))
                .failureHandler(routingContext -> {
                    if (routingContext.statusCode() == 403) {
                        routingContext
                                .response()
                                .setStatusCode(routingContext.statusCode())
                                .end("You are not authorized to access this resource");
                    }
                    else
                    {
                        routingContext
                                .response()
                                .setStatusCode(routingContext.statusCode())
                                .end("You are not logged in");
                    }
                });

        router.get("/protected/admin/*")
                .handler(AuthAndAuthorization::checkRole);

        router.get("/protected/data").handler(ctx -> {

            String username = ctx.user().principal().getString("sub");

            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("message", "Hello " + username + " " + ctx.user().principal().getString("role")).toBuffer());
        });

        router.get("/protected/admin/data").handler(ctx -> {

            String username = ctx.user().principal().getString("sub");

            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("message", "Hello " + username).toBuffer());
        });

        // Simulate login
        router.post("/login").handler(ctx -> {

            String username = ctx.request().getParam("username");

            String password = ctx.request().getParam("password");

            if (username == null || password == null)
            {
                ctx.response().setStatusCode(400).end("Username and password required");

                return;
            }

            String token = jwtAuth.generateToken(
                        new JsonObject()
                                .put("sub", username)
                                .put("role" ,"user"),
                        new JWTOptions().setExpiresInMinutes(30));

            ctx.response().end(new JsonObject()
                    .put("token" , token)
                    .toBuffer());
        }).failureHandler(routingContext -> {
            routingContext
                    .response()
                    .setStatusCode(routingContext.statusCode())
                    .end("Please login again");
        });

        vertx.createHttpServer().requestHandler(router).listen(8080, res -> {
            if (res.succeeded())
            {
                System.out.println("JWT Server running at " + res.result().actualPort());
            }
            else
            {
                res.cause().printStackTrace();
            }
        });
    }

    public static void checkRole(RoutingContext ctx)
    {
        String role = ctx.user().principal().getString("role");

        if (role == null || !role.equals("admin"))
        {
            ctx.fail(403);

            return;
        }

        ctx.next();
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new AuthAndAuthorization());
    }

}
