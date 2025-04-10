package org.example.starting;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;

public class BodyHandlerDemo {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        //BodyHandler auto calls ctx.next() that's why
        // we are able to get the body in the next handler using context
        //router.route().handler(BodyHandler.create().setBodyLimit(1024 * 1024 * 10).setMergeFormAttributes(false));

//        router.post("/submit").handler(routingContext -> {
//
//            String name = routingContext.request().getParam("name");
//
//            //after setting setMergeFormAttributes(false) otherwise we access as above
//            String name1 = routingContext.request().formAttributes().get("name");
//
//            RequestBody body = routingContext.body();
//
//            System.out.println("Received request body: " + body);
//
//            // Send a response
//            routingContext.response().end("Request received: " + name + " " + name1);
//        }).failureHandler(routingContext -> {
//            routingContext.response().setStatusCode(routingContext.statusCode()).end("Body is too laarge!!!");
//        });

        router.post("/fileUpload")
                .handler(BodyHandler.create().setDeleteUploadedFilesOnEnd(true))
                .handler(routingContext -> {

                    List<FileUpload> fileUploadList = routingContext.fileUploads();

                    for (FileUpload upload : fileUploadList)
                    {
                        System.out.println("File uploaded:");
                        System.out.println("Name: " + upload.name());
                        System.out.println("Filename: " + upload.fileName());
                        System.out.println("Size in bytes: " + upload.size());
                        System.out.println("Saved at: " + upload.uploadedFileName());
                    }

                    routingContext.response().setStatusCode(200).end("File uploaded successfully");
                }).failureHandler(routingContext -> {
                     routingContext.response().setStatusCode(routingContext.statusCode()).end("Body is too laarge!!!");
                });

        // Start the HTTP server
        vertx.createHttpServer().requestHandler(router).listen(8080, ar -> {
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
