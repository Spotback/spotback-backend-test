package com.spotback.verticles;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HttpVerticle extends AbstractVerticle {
    private static Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class);

    public void start(Future<Void> startFuture) throws Exception {
        DeploymentOptions worker = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle(new CreateAccountVerticle(), worker);
        vertx.deployVerticle(new ReadAccountVerticle(), worker);
        vertx.deployVerticle(new UpdateAccountVerticle(), worker);
        vertx.deployVerticle(new DeleteAccountVerticle(), worker);
        Future<Void> steps = startHttpServer();
        steps.setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    public Future<Void> startHttpServer() {
        Future<Void> future = Future.future();
        Router router = Router.router(vertx);
        router.post().handler(this::requestHandler);
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(req -> {
            LOGGER.info("INCOMING REQUEST AT: " + req.absoluteURI());
            router.accept(req);
        }).listen(8880, ar -> {
            if (ar.succeeded()) {
                LOGGER.info("HTTP server is running on port 8880");
                future.complete();
            } else {
                LOGGER.error("Could not start a HTTP server: " + ar.cause());
                future.fail(ar.cause());
            }
        });
        return future;
    }

    private void requestHandler(RoutingContext context) {
        context.request().bodyHandler(bodyHandler -> {
            MultiMap headers = context.request().headers();
            DeliveryOptions options = new DeliveryOptions().setHeaders(headers);
            vertx.eventBus().send(context.request().uri(), bodyHandler.toJsonObject(), options, resp -> {
                if (resp.succeeded()) {
                    context.response().end(resp.result().body().toString());
                } else {
                    context.response().end("Request failed");
                }
            });
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpVerticle());
    }
}
