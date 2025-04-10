package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.Router;

public class CookieDemo {

    public static void main(String[] args)
    {
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

        router.route("/set-cookie").handler(routingContext -> {

            Cookie cookie = Cookie.cookie("hello", "xyz123")
                    .setHttpOnly(true)  // Not accessible by JS
                    .setSecure(true) // sent only over HTTPS connections
                    .setMaxAge(3600);// 1 hour

            routingContext
                    .response()
                    .addCookie(Cookie.cookie("myCookie", "cookieValue"))
                    .addCookie(cookie)
                    .end("Cookie set successfully");
        });

        router.route("/get-cookie").handler(routingContext -> {

            String myCookie = routingContext
                    .request()
                    .getCookie("myCookie").getValue();

            String myCookie2 = routingContext
                    .request()
                    .getCookie("hello").getValue();

            routingContext.response().end(myCookie + " " + myCookie2);
        });

        router.route("/remove-cookie").handler(routingContext -> {

            routingContext.response()
                    .addCookie(Cookie.cookie("myCookie", "").setMaxAge(0))
                    .end("removed cookie successfully");
        });
    }
}
