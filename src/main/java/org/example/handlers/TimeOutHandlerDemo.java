package org.example.handlers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TimeoutHandler;

public class TimeOutHandlerDemo extends AbstractVerticle {

    @Override
    public void start()
    {
        Router router = Router.router(vertx);

        router.route("/foo/*")
                .handler(TimeoutHandler.create(5000))
                .failureHandler(routingContext -> {
                        routingContext
                                .response()
                                .setStatusCode(routingContext.statusCode())
                                .end("Request timed out");
                });

        router.route("/foo/bar").handler(ctx -> {
            vertx.setTimer(9000, tid -> {
                if (!ctx.response().ended()) {
                    ctx.response().end("This will not be reached due to timeout");
                }
            });
        });

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

    @Override
    public void stop() {
        // Cleanup logic if needed
        System.out.println("TimeOutHandlerDemo stopped");
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new TimeOutHandlerDemo());
    }
}
