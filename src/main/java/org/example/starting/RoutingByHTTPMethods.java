package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RoutingByHTTPMethods {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        Route route1 = router.route().method(HttpMethod.GET);

        route1.handler(ctx -> {
        });

        Route route2 = router.route(HttpMethod.POST, "/some/path/");

        route2.handler(ctx -> {
        });

        router.get().handler(ctx -> {
        });

        router.get("/some/path/").handler(ctx -> {
        });

        router.getWithRegex(".*foo").handler(ctx -> {
        });

        Route route3 = router.route("/home/path").method(HttpMethod.POST).method(HttpMethod.PUT);

        route3.handler(ctx -> {
            HttpMethod method = ctx.request().method();
            ctx.response().setStatusCode(200).end(method.name());
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
