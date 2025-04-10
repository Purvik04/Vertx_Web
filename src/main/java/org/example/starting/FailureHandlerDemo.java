package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class FailureHandlerDemo {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

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

        router.get("/somepath/path1").handler(ctx -> {
            // Let's say this throws a RuntimeException
            throw new RuntimeException("something happened!");
        });

        router.get("/somepath/path2").handler(ctx -> {
            // This one deliberately fails the request passing in the status code
            // E.g. 403 - Forbidden
            ctx.fail(403);
        });

        router.get("/somepath/path2").failureHandler(ctx -> {
            ctx.response().setStatusCode(ctx.statusCode()).end("failure from particular path's failure handler");
        });

        // Define a failure handler
        // This will get called for any failures in the above handlers
        router.get("/somepath/*").failureHandler(failureContext -> {

            int statusCode = failureContext.statusCode();
            // Status code will be 500 for the RuntimeException
            // or 403 for the other failure
            failureContext.response().setStatusCode(statusCode).end("Sorry! Not today");
        });
    }
}