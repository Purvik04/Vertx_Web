package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.example.starting.subrouter.ProductRouter;

public class RouteMatchFailures {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        router.get("/home").consumes("text/plain").handler(routingContext -> {
            routingContext.response().end("Hello World");
        });

        // Start the HTTP server
        vertx.createHttpServer().requestHandler(router).listen(8080, "localhost", ar -> {
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
