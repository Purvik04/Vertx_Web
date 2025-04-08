package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.Map;

public class ContextDataDemo {
    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));

        Router router = Router.router(vertx);

        router.route("/context").handler(routingContext -> {

            routingContext.put("name", "purvik");

            routingContext.next();
        });

        router.route("/context").handler(routingContext -> {

            String value = routingContext.get("name");

            Map<String, Object> data = routingContext.data();

            JsonObject obj = new JsonObject(data);

            routingContext.response().putHeader("content-type", "application/json").end(obj.toString());
        });

        // Start the server
        httpServer.requestHandler(router).listen();
    }
}
