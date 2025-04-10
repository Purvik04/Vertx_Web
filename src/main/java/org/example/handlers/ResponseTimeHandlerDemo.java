package org.example.handlers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ResponseTimeHandler;

public class ResponseTimeHandlerDemo extends AbstractVerticle {

    @Override
    public void start()
    {
        Router router = Router.router(vertx);

        router.route("/foo/*")
                .handler(ResponseTimeHandler.create());


        router.route("/foo/bar").handler(ctx -> {
            vertx.setTimer(9000, tid -> {
                ctx.response().end("Hello after 9 seconds");
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
        Vertx.vertx().deployVerticle(new ResponseTimeHandlerDemo());
    }
}
