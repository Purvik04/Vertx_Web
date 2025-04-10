package org.example.statichandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StaticHandlerDemo extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route("/static/*")
                .handler(StaticHandler.create("webroot")
                        .setIndexPage("index.html") //Defines the default file to serve when someone accesses a directory path like /static/.
                        .setCachingEnabled(false)
                        /*
                        Turns HTTP browser caching on or off.

                        When true (default), the handler adds headers like Cache-Control, Last-Modified, etc.,
                        to help browsers cache files.

                        This disables browser caching — the browser fetches the file fresh every time.

                        Why use this?
                        ✅ Turn it off during development so changes are reflected instantly.
                        ❌ Don’t use this in production — caching improves performance.
                        */
                        .setMaxAgeSeconds(3600)
                        /*
                        Sets how long (in seconds) a browser should cache a file.

                        Default: 86,400 seconds (1 day)
                        This sets Cache-Control: max-age=3600
                        */
                        .setDirectoryListing(true)
                        /*
                        Allows users to see the contents of a directory when there's no index.html.

                        Default: false (recommended)

                        📌 Now a request to /static/assets/ will show a list of files in that folder.

                        🔎 Why use this?
                        For testing/debugging or internal tools. Don't use in production unless you're okay exposing file structure.
                        */
                        .setFilesReadOnly(false)
                        /*
                        🧠 What it does:
                        If true (default), Vert.x assumes files never change while the server is running.

                        If false, Vert.x will re-check file timestamps to update cache entries.

                        📌 Now Vert.x will detect if files change on disk (useful in dev).

                        🔎 Why use this?
                        ✅ Turn it off in development or live-editing situations

                        ✅ Leave it on in production for faster file serving
                        */
                        .setMaxCacheSize(500)
                        /*
                        setMaxCacheSize(int maxCacheSize)
                        🧠 What it does:
                        Sets how many files Vert.x can keep in memory cache.

                        Default: 1000

                        📌 This limits in-memory caching to 500 files.

                        🔎 Why use this?
                        To control memory usage, especially on systems with limited RAM.*/
                        .setCacheEntryTimeout(60000)
                        /*
                        🧠 What it does:
                        Sets how long (in milliseconds) Vert.x will keep a file in memory cache before checking disk for changes.

                        📌 After 1 minute, Vert.x will re-check if the file changed.*/
                );


        vertx.createHttpServer().requestHandler(router).listen(8080,"localhost", ar -> {
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

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new StaticHandlerDemo());
    }
}
