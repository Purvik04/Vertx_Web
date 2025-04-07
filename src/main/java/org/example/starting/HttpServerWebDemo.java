package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class HttpServerWebDemo {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        Route route = router.route("/home");

        route.handler(routingContext -> {

            HttpServerResponse httpServerResponse = routingContext.response().setChunked(true);

            httpServerResponse.write("Kem\n");

            routingContext.vertx().setTimer(3000, id -> {
                routingContext.next();
            });
        });

        route.handler(routingContext -> {

            HttpServerResponse httpServerResponse = routingContext.response();

            httpServerResponse.write("chho\n");

            routingContext.vertx().setTimer(3000, id -> {
                routingContext.next();
            });
        });

        route.handler(routingContext -> {

            HttpServerResponse httpServerResponse = routingContext.response();

            httpServerResponse.write("bhai?\n");

            routingContext.response().end();
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
