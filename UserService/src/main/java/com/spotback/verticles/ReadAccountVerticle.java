package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReadAccountVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadAccountVerticle.class);
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("/readAccount", this::readAccount);
        consumer.completionHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    private boolean readAccount(Message<JsonObject> message) {
        LOGGER.info("REQUEST RECEIVED IN READ ACCOUNT VERTICLE.");
        AtomicBoolean succeeded = new AtomicBoolean(false);
        if(message.body().isEmpty()) {
            message.reply("Not authorized to make this request.");
            succeeded.set(false);
            return succeeded.get();
        } else {
            /**TODO
             * Implement extracting of the json to pojo and validating
             */
            vertx.executeBlocking(future -> {
                try {
                    /**TODO
                     * get data from the db
                     */
                } catch (Exception ex) {
                    LOGGER.error("Failed to get data from Dynamo", ex);
                    message.fail(500, "Exception in connecting to Dynamo");
                    future.fail(ex);
                    succeeded.set(false);
                }
                future.complete();
            }, res -> {
                if (res.succeeded()){
                    message.reply("Account retrieved successfully");
                    succeeded.set(true);
                } else {
                    message.fail(500, "Account retrieval failed. " + res.cause());
                    succeeded.set(false);
                }
            });
            return succeeded.get();
        }
    }
}
