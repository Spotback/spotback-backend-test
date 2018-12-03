package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
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
		router.post("/createAccount").handler(this::createAccount);
		router.post("/readAccount").handler(this::readAccount);
		router.post("/updateAccount").handler(this::updateAccount);
		router.post("/deleteAccount").handler(this::deleteAccount);
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

	private void createAccount(RoutingContext context) {
		context.request().bodyHandler(bodyHandler -> {
			final JsonObject body = bodyHandler.toJsonObject();
			vertx.eventBus().send("create.Account", body, resp -> {
				if(resp.succeeded()) {
					context.response().end(resp.result().body().toString());
				} else {
					context.response().end("Account creation failed.");
				}
			});
		});
	}

	private void readAccount(RoutingContext context) {
		context.request().bodyHandler(bodyHandler -> {
			final JsonObject body = bodyHandler.toJsonObject();
			vertx.eventBus().send("read.Account", body, resp -> {
				if(resp.succeeded()) {
					context.response().end(resp.result().body().toString());
				} else {
					context.response().end("Account retrieval failed.");
				}
			});
		});
	}

	private void updateAccount(RoutingContext context) {
		context.request().bodyHandler(bodyHandler -> {
			final JsonObject body = bodyHandler.toJsonObject();
			vertx.eventBus().send("update.Account", body, resp -> {
				if(resp.succeeded()) {
					context.response().end(resp.result().body().toString());
				} else {
					context.response().end("Account update failed.");
				}
			});
		});
	}

	private void deleteAccount(RoutingContext context) {
		context.request().bodyHandler(bodyHandler -> {
			final JsonObject body = bodyHandler.toJsonObject();
			vertx.eventBus().send("delete.Account", body, resp -> {
				if(resp.succeeded()) {
					context.response().end(resp.result().body().toString());
				} else {
					context.response().end("Account deletion failed.");
				}
			});
		});
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new HttpVerticle());
	}
}
