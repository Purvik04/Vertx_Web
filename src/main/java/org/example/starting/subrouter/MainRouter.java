// MainRouter.java
package org.example.starting.subrouter;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MainRouter {

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        Router mainRouter = Router.router(vertx);

        Router productRouter = new ProductRouter(vertx).getRouter();

        mainRouter.route("/productApi/*").subRouter(productRouter);

        // Start the HTTP server
        vertx.createHttpServer().requestHandler(mainRouter).listen(8080, "localhost", ar -> {
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
}
