package org.example.handlers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;

public class ContenTypeHandlerDemo extends AbstractVerticle {

    @Override
    public void start()
    {
        Router router = Router.router(vertx);

        router.route("/foo/*")
                .handler(ResponseContentTypeHandler.create());

        router.route("/api/*").handler(ResponseContentTypeHandler.create());

        router.get("/api/books")
                .produces("text/xml")
                .produces("application/json")
                .handler(ctx -> {
                    if (ctx.getAcceptableContentType().equals("application/json")) {
                        ctx.response().end("{\"message\": \"Hello from JSON\"}");
                    }
                    else{
                        ctx.response().end("<message>Hello from XML</message>");
                    }
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
        Vertx.vertx().deployVerticle(new ContenTypeHandlerDemo());
    }
}
