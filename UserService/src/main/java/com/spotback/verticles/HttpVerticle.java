package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HttpVerticle extends AbstractVerticle {
	private static Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class);

	public void start(Future<Void> startFuture) throws Exception {
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
		router.get("/endpoint1").handler(this::endpoint1);
		router.post("/endpoint2").handler(this::endpoint2);
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

	public void endpoint1(RoutingContext context) {

	}

	public void endpoint2(RoutingContext context) {

	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new HttpVerticle());
	}
}
