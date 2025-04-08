package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

public class RoutingMIMEContentType {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        // For JSON payloads
        router.route("/submit")
                .consumes("application/json")
                .handler(ctx -> {
                    ctx.response().end("Got JSON!");
                });

        // For form submissions
        router.route("/submit")
                .consumes("application/x-www-form-urlencoded")
                .handler(ctx -> {
                    ctx.response().end("Got form data!");
                });

        // For plain text
        router.route("/submit")
                .consumes("text/plain")
                .handler(ctx -> {
                    ctx.response().end("Got plain text!");
                });

        router.route().consumes("text/*").handler(ctx -> {
            // Matches text/plain, text/html, etc.
        });

        router.route().consumes("*/json").handler(ctx -> {
            // Matches application/json, text/json, etc.
        });

        httpServer.requestHandler(router).listen().onComplete(ar -> {
            if (ar.succeeded())
            {
                System.out.println("HTTP server started on port " + httpServer.actualPort());
            }
            else
            {
                System.err.println("Failed to start HTTP server: " + ar.cause());
            }
        });
    }
}
