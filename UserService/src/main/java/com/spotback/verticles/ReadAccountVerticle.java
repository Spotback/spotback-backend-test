package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class ReadAccountVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("read.Account", this::readAccount);
        startFuture.complete();
    }

    private void readAccount(Message<JsonObject> message) {
    }
}
