package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class CreateAccountVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("create.Account", this::createAccount);
        startFuture.complete();
    }

    private void createAccount(Message<JsonObject> message) {
    }

}
