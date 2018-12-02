package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class DeleteAccountVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("delete.Account", this::deleteAccount);
        startFuture.complete();
    }

    private void deleteAccount(Message<JsonObject> message) {
    }

}
