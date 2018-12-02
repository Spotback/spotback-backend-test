package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class UpdateAccountVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("update.Account", this::updateAccount);
        startFuture.complete();
    }

    private void updateAccount(Message<JsonObject> message) {
    }
}
