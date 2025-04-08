package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class PathParamsAndRegexPath {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        HttpServerOptions options = new HttpServerOptions();

        options.setHost("localhost")
                .setPort(8080);

        HttpServer httpServer = vertx.createHttpServer(options);

        Router router = Router.router(vertx);

         router.route(HttpMethod.GET, "/catalogue/products/:productType/:productID/")
                 .handler(routingContext -> {

                        String param1 = routingContext.request().getParam("productType");

                        String param2 = routingContext.request().getParam("productID");

                        routingContext.response()
                                .putHeader("content-type", "application/json")
                                .end(new JsonObject()
                                        .put("productType", param1)
                                        .put("productID", param2)
                                        .toBuffer());
                 });

        router.route(HttpMethod.GET, "/flights/:from-:to")
                .handler(ctx -> {
                    // when handling requests to /flights/AMS-SFO will set:
                    String from = ctx.pathParam("from"); // AMS

                    String to = ctx.pathParam("to"); // SFO
                    /*
                    remember that this will not work as expected when the parameter
                    naming pattern in use is not the "extended" one. That is because in
                    that case "-" is considered to be part of the variable name and
                    not a separator.
                    */
                });

//        router.route().pathRegex(".*foo").handler(ctx -> {
//            // This handler will be called for:
//
//            // /some/path/foo
//            // /foo
//            // /foo/bar/wibble/foo
//            // /bar/foo
//            // /nnroubfoiwfoo
//
//            // But not:
//            // /bar/wibble
//            ctx.response()
//                    .putHeader("content-type", "plain/text")
//                    .end("foo");
//        });

        router.routeWithRegex(".*foo").handler(ctx -> {

            // This handler will be called same as previous example

            ctx.response()
                    .putHeader("content-type", "plain/text")
                    .end("foo");
        });

        //if i register two route with same path, then the first registerd one will be used
        //note:- give specific path above and then generalized path
        //Capturing path parameters with regular expressions
        router.route().pathRegex("\\/([^\\/]+)\\/([^\\/]+)").handler(ctx -> {

            String productType = ctx.pathParam("param0");
            String productID = ctx.pathParam("param1");

            ctx.response()
                    .putHeader("content-type", "plain/text")
                    .end("Params: " + productType + ", " + productID);
        });

        /*
        Using int index param names might be troublesome in some cases.
        It’s possible to use named capture groups in the regex path.

        we can still access group parameters as we would with normal groups (i.e. params0, params1, etc.)
        */
        router.routeWithRegex("\\/(?<productType>[^\\/]+)\\/(?<productID>[^\\/]+)")
                .handler(ctx -> {

                    String productType = ctx.pathParam("productType");
                    String productID = ctx.pathParam("productID");

                    // Do something with them...
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
