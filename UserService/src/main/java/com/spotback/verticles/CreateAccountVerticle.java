package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class CreateAccountVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAccountVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("create.Account", this::createAccount);
        consumer.completionHandler(ar -> {
                    if (ar.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(ar.cause());
                    }
                });
    }


    public boolean createAccount(Message<JsonObject> message) {
        LOGGER.info("REQUEST RECEIVED IN CREATE ACCOUNT VERTICLE.");
        AtomicBoolean persisted = new AtomicBoolean(false);
        if(message.body().isEmpty()) {
            message.reply("Not authorized to make this request.");
            persisted.set(false);
            return persisted.get();
        } else {
            /**TODO
             * Implement extracting of the json to pojo and validating
             */
            vertx.executeBlocking(future -> {
                try {
                    /**TODO
                     * Persist data in the db
                     */
                } catch (Exception ex) {
                    LOGGER.error("Failed to send data to Dynamo", ex);
                    message.fail(500, "Exception in persisting data to Dynamo");
                    future.fail(ex);
                    persisted.set(false);
                }
                future.complete();
            }, res -> {
                if (res.succeeded()){
                    message.reply("Account created successfully");
                    persisted.set(true);
                } else {
                    message.fail(500, "Account creation failed");
                    persisted.set(false);
                }
            });
            return persisted.get();
        }
    }

}
