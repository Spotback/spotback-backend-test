package com.spotback.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class CreateAccountVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAccountVerticle.class);
    private static MailClient mailClient;
    private static MailMessage eMessage = new MailMessage();
    private static MailConfig config = new MailConfig();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("/createAccount", this::createAccount);
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
        if (message.body().isEmpty()) {
            message.reply("Not authorized to make this request.");
            persisted.set(false);
            return persisted.get();
        } else {
            vertx.executeBlocking(future -> {
                try {
                    /**TODO
                     * Persist data in the db
                     */
                    getEmailClient().sendMail(eMessage, ar -> {
                        if (ar.succeeded()) {
                            LOGGER.info(ar.result());
                        } else {
                            LOGGER.error(ar.cause());
                        }
                    });
                } catch (Exception ex) {
                    LOGGER.error("Failed to send data to Dynamo", ex);
                    message.fail(500, "Exception in persisting data to Dynamo");
                    future.fail(ex);
                    persisted.set(false);
                }
                future.complete();
            }, res -> {
                if (res.succeeded()) {
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

    private static MailClient getEmailClient() {
        config.setHostname("mail.example.com");
        config.setPort(587);
        config.setStarttls(StartTLSOptions.REQUIRED);
        config.setUsername("spotbackteam@gmail.com");
        config.setPassword("Spotbackteam!");
        mailClient = MailClient.createNonShared(Vertx.vertx(), config);
        eMessage.setFrom("spotbackteam@gmail.com (Spotback Team)");
        eMessage.setTo("dylancorbus@outlook.com");
        eMessage.setSubject("Test email");
        eMessage.setText("this is the plain message text");
        eMessage.setHtml("this is html text <a href=\"http://vertx.io\">vertx.io</a>");
        return mailClient;
    }

}
