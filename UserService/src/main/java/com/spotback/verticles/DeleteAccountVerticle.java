package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeleteAccountVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAccountVerticle.class);
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("/deleteAccount", this::deleteAccount);
        consumer.completionHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    private boolean deleteAccount(Message<JsonObject> message) {
        LOGGER.info("REQUEST RECEIVED IN DELETE ACCOUNT VERTICLE.");
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
                     * delete data in the db
                     */
                } catch (Exception ex) {
                    LOGGER.error("Failed to delete data to Dynamo", ex);
                    message.fail(500, "Exception in deleting data in Dynamo");
                    future.fail(ex);
                    persisted.set(false);
                }
                future.complete();
            }, res -> {
                if (res.succeeded()){
                    message.reply("Account deleted successfully");
                    persisted.set(true);
                } else {
                    message.fail(500, "Account delete failed to persist. " + res.cause());
                    persisted.set(false);
                }
            });
            return persisted.get();
        }
    }

}
