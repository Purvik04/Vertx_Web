package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RoutingByPaths {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        Route route = router.route().path("/some/path/");

        route.handler(ctx -> {
            // This handler will be called for the following request paths:

            // `/some/path/`
            // `/some/path//`
            //
            // but not:
            // `/some/path` the end slash in the path makes it strict
            // `/some/path/subdir`
        });

        // paths that do not end with slash are not strict
        // this means that the trailing slash is optional
        // and they match regardless
        Route route2 = router.route().path("/some/path");

        route2.handler(ctx -> {
            // This handler will be called for the following request paths:

            // `/some/path`
            // `/some/path/`
            // `/some/path//`
            //
            // but not:
            // `/some/path/subdir`
        });

        Route route3 = router.route().path("/some/path/*");

        route3.handler(ctx -> {
            // This handler will be called for any path that starts with
            // `/some/path/`, e.g.

            // `/some/path/`
            // `/some/path/subdir`
            // `/some/path/subdir/blah.html`
            //
            // but **ALSO**:
            // `/some/path` the final slash is always optional with a wildcard to preserve
            //              compatibility with many client libraries.
            // but **NOT**:
            // `/some/patha`
            // `/some/patha/`
            // etc...
        });

        router.route("/home/*").handler(ctx->{
            ctx.response().end("Hello World");
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
