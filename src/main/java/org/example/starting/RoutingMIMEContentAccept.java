package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class RoutingMIMEContentAccept {

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
                .produces("application/json")
                .handler(ctx -> {
                    ctx.response().putHeader("content-type", "application/json")
                            .end(new JsonObject().put("name" , "purvik").toBuffer());
                });

        // For plain text
        router.route("/submit")
                .produces("text/plain")
                .handler(ctx -> {
                    ctx.response().putHeader("content-type", "text/plain").end("Send plain text");
                });

        router.route("/data")
                .produces("application/json")
                .produces("application/xml")
                .handler(ctx -> {

                    String contentType = ctx.getAcceptableContentType(); // Best match from Accept header

                    if ("application/xml".equals(contentType))
                    {
                        ctx.response()
                                .putHeader("Content-Type", contentType)
                                .end("<message>Hello from XML</message>");
                    }
                    else
                    {
                        ctx.response()
                                .putHeader("Content-Type", "application/json")
                                .end("{\"message\": \"Hello from JSON\"}");
                    }
                });

        router.route("/submit")
                .consumes("application/json")     // Accepts JSON input
                .produces("application/xml")      // Sends XML response
                .handler(ctx -> {
                    // parse JSON input, then return XML
                    ctx.response()
                            .putHeader("Content-Type", "application/xml")
                            .end("<response><message>Hello</message></response>");
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
