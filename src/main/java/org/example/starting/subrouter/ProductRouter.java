package org.example.starting.subrouter;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ProductRouter {

    private final Router router;

    public ProductRouter(Vertx vertx)
    {
        this.router = Router.router(vertx);

        // Define handlers
        router.get("/products/:productID").handler(ctx -> {
            ctx.response().end("Get product " + ctx.pathParam("productID"));
        });

        router.put("/products/:productID").handler(ctx -> {
            ctx.response().end("Create/Update product " + ctx.pathParam("productID"));
        });

        router.delete("/products/:productID").handler(ctx -> {
            ctx.response().end("Delete product " + ctx.pathParam("productID"));
        });
    }

    public Router getRouter() {
        return router;
    }
}
