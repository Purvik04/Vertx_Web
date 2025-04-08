package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class BlockingHandlers {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        Route route1 = router.get("/blocking1");

        Route route2 = router.get("/blocking2");

        route1.blockingHandler(request -> {

            HttpServerResponse response = request.response();

            response.putHeader("content-type", "text/plain");

            response.setStatusCode(200).end("Blocking Handler 1");

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        });

        route2.blockingHandler(request -> {

            HttpServerResponse response = request.response();

            response.putHeader("content-type", "text/plain");

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            response.setStatusCode(200).end("Blocking Handler 2");
        });

        router.post("/some/endpoint").handler(ctx -> {

            ctx.request().setExpectMultipart(true);

            ctx.next();
        }).blockingHandler(ctx -> {
            // ... Do some blocking operation

            ctx.response().putHeader("content-type", "text/plain").end("Blocking operation completed");
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
