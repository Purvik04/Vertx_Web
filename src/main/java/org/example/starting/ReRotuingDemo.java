package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class ReRotuingDemo {

    public static void main(String[] args) {
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

        // Initial route
        router.get("/start").handler(ctx -> {

            ctx.put("foo", "bar");
            // instead of ctx.next(), jump to “/next”
            ctx.reroute("/next");
        });

        // The “/next” route
        router.get("/next").handler(ctx -> {
            // we still have foo=bar in the same RoutingContext
            ctx.response().end("Rerouted! foo=" + ctx.get("foo"));
        });

        // Jump to POST /do-it
        //note: necessary to set the method to POST in the reroute
        router.get("/go").handler(ctx -> ctx.reroute(HttpMethod.POST, "/do-it"));

        router.post("/do-it").handler(ctx -> {
            // do something “destructive” here
            ctx.response().end("Did it!");
        });

        // A “pretty” 404 page
        router.get("/not-found-page").handler(ctx -> {
            ctx.response()
                    .setStatusCode(404)
                    .end("<h1>Oops — resource not found!</h1>");
        });

        // Catch-all failure handler
        router.route("/failCheck").handler(ctx->{
            // do something that might fail
            ctx.fail(404);
        }).failureHandler(ctx -> {
            if (ctx.statusCode() == 404)
            {
                // reroute into your pretty page
                ctx.reroute("/not-found-page");
            }
            else
            {
                // other errors: let default behavior happen
                ctx.next();
            }
        });

        router.get("/step1/:user/search").handler(ctx -> {

            ctx.put("user", ctx.pathParam("user"));// from path, e.g. :user

            ctx.put("name", ctx.request().getParam("name"));      // from query param

            ctx.reroute("/step2");
        });

        router.get("/step2").handler(ctx -> {

            String user = ctx.get("user");

            String name = ctx.get("name");

            ctx.response().end("Hello " + user + ", you searched for: " + name);
        });

    }
}
