package messageTransport.broker;

import messageTransport.model.Message;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageBroker {
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
            super.notify();
        } catch(final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Message consume() {
        try {
            while (this.messagesConsumed.isEmpty()) {
                super.wait();
            }
            final Message consumedMessage = this.messagesConsumed.poll();
            super.notify();
            return consumedMessage;
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
