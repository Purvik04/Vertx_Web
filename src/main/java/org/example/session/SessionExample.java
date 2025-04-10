package org.example.session;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class SessionExample {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        // Session setup
        SessionHandler sessionHandler = SessionHandler.create(LocalSessionStore.create(vertx));

        router.route().handler(BodyHandler.create()); // for form data

        router.route().handler(sessionHandler); // enable session support

        // Simulate login
        router.post("/login").handler(ctx -> {

            String username = ctx.request().getParam("username");

            if (username == null || username.isEmpty())
            {
                ctx.response().setStatusCode(400).end("Username required");

                return;
            }

            ctx.session().put("username", username); // Store in session

            ctx.response().end("Logged in as " + username);
        });

        // Simulate a protected page
        router.get("/dashboard").handler(ctx -> {

            String user = ctx.session().get("username");

            if (user == null)
            {
                ctx.response().setStatusCode(401).end("Please login first.");
            }
            else
            {
                ctx.response().end("Welcome to your dashboard, " + user);
            }
        });

        // Logout and destroy session
        router.get("/logout").handler(ctx -> {

            ctx.session().destroy();

            ctx.response().end("Logged out");
        });

        vertx.createHttpServer().requestHandler(router).listen(8080 , "localhost", ar -> {
            if (ar.succeeded())
            {
                System.out.println("Server started on port 8080");
            }
            else
            {
                System.out.println("Failed to start server: " + ar.cause());
            }
        });
    }
}

