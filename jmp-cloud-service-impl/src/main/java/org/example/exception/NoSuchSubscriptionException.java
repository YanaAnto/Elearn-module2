package org.example.exception;

public class NoSuchSubscriptionException extends RuntimeException {

    public NoSuchSubscriptionException() {
        super("Subscription not found!");
    }
}
