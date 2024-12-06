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
        this.messagesConsumed.add(message);
    }

    public synchronized Message consume() {
        return this.messagesConsumed.poll();
    }
}
