package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class HelperFunctions {
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

        router.route("/home").handler(routingContext -> {
            routingContext.redirect("https://google.com/");
        });

        router.route("/getJson").handler(routingContext -> {
            routingContext.json(new JsonObject().put("message", "Hello World"));
        });

        router.route("/getJsonArray").handler(routingContext -> {
            routingContext.json(new JsonArray()
                    .add("message")
                    .add("Hello World")
                    .add(new JsonObject().put("message", "Hello World")));
        });

        router.route("/getSomeObj").handler(routingContext -> {
            System.out.println(routingContext.is("application/json"));
            routingContext.json(new Person("purvik", 22));
        });

        router.route("/getFile").handler(routingContext -> {

            // Set the Content-Disposition header to prompt download
//            routingContext.response().putHeader("Content-Disposition", "attachment; filename=" + "hello.txt");
            //shortcut version
            routingContext.attachment("hello.txt");

            // Serve the file
            routingContext.response().sendFile("/home/purvik/IdeaProjectsUltimate/VertxWeb/hello.txt");
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

class Person{
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
