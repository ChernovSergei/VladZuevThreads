package messageTransport.broker;

import messageTransport.model.Message;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static java.util.Optional.of;

public class MessageBroker {
    private static final String MSG_PRODUCED = "Message '%s' is produced.\n";
    private static final String MSG_TEMPLATE = "Message '%s' is consumed.\n";
    private final Queue<Message> messagesConsumed;
    private final int maxMessages;

    public MessageBroker(final int maxMessages) {
        this.messagesConsumed = new ArrayDeque<>(maxMessages);
        this.maxMessages = maxMessages;
    }

    public synchronized void produce(final Message message) {
        try {
            while (this.messagesConsumed.size() >= this.maxMessages) {
                super.wait();

            }
            this.messagesConsumed.add(message);
            System.out.printf(MSG_PRODUCED, message);
            super.notify();
        } catch(final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume() {
        try {
            while (this.messagesConsumed.isEmpty()) {
                super.wait();
            }
            final Message consumedMessage = this.messagesConsumed.poll();
            System.out.printf(MSG_TEMPLATE, consumedMessage);
            super.notify();
            return of(consumedMessage);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
