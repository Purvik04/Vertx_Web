package org.example.starting;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class SimpleResponsesDemo {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer(new HttpServerOptions()
                .setHost("localhost")
                .setPort(8080));

        Router router = Router.router(vertx);

        server.requestHandler(router).listen().onComplete(result -> {
            if (result.succeeded())
            {
                System.out.println("Server started on port " + server.actualPort());
            }
            else
            {
                System.err.println("Failed to start server: " + result.cause());
            }
        });

        /*
        Simplifies Code: Reduces boilerplate code by handling common tasks like serialization
        and header setting.

        Consistent Error Handling: Ensures that errors are managed uniformly,
        providing consistent responses to clients.

        Flexibility: While optimized for JSON responses,
        it also supports other response types when you manage the response writing within the handler.
        */

        router.get("/hello").respond(ctx -> Future.succeededFuture(new JsonObject().put("message", "Hello, World!")));

        User user = new User("Alice", 30);

        router.get("/user").respond(ctx -> Future.succeededFuture(user));

        router.get("/plaintext").respond(ctx -> {
            ctx.response()
                    .putHeader("Content-Type", "text/plain")
                    .end("Hello, World!");
            return Future.succeededFuture();
        });
    }
}

class User
{
    private String name;
    private int age;

    public User(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    //for serialization using Jackson DataBinder
    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }
}


