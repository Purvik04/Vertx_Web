package org.example.recap;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class HttpServerDemo {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        httpServer.requestHandler(httpServerRequest -> {

            String path = httpServerRequest.path();

            HttpServerResponse response = httpServerRequest.response();

            response.putHeader("content-type", "text/plain");

            if ("/home".equals(path) || "/".equals(path))
            {
                response.setStatusCode(200).end("Welcome to the Home Page!");
            }
            else if ("/about".equals(path))
            {
                response.setStatusCode(200).end("Welcome to the about Page!");
            }
            else
            {
                response.setStatusCode(404).end("Page not found");
            }
        });

        httpServer.listen().onComplete(result -> {
            if (result.succeeded())
            {
                System.out.println("HTTP server started on port " + httpServer.actualPort());
            }
            else
            {
                System.err.println("Failed to start HTTP server: " + result.cause());
            }
        });
    }
}
