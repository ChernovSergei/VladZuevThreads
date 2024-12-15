package messageTransport.broker;

import messageTransport.consumer.MessageConsuming;
import messageTransport.model.Message;
import messageTransport.producer.MessageProducingTask;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class MessageBroker {
    private static final String MSG_PRODUCED = "Message '%s' is produced by producer'%s'. Amount of messages before producing: %d.\n";
    private static final String MSG_TEMPLATE = "Message '%s' is consumed by consumer'%s'. Amount of messages before consuming: %d.\n";
    private final Queue<Message> messagesConsumed;
    private final int maxMessages;

    public MessageBroker(final int maxMessages) {
        this.messagesConsumed = new ArrayDeque<>(maxMessages);
        this.maxMessages = maxMessages;
    }

    public synchronized void produce(final Message message, final MessageProducingTask producingTask) {
        try {
            while (this.messagesConsumed.size() >= this.maxMessages) {
                super.wait();

            }
            this.messagesConsumed.add(message);
            System.out.printf(MSG_PRODUCED, message, producingTask.getName(), this.messagesConsumed.size() - 1);
            super.notifyAll();
        } catch(final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume(final MessageConsuming messageConsuming) {
        try {
            while (this.messagesConsumed.isEmpty()) {
                super.wait();
            }
            final Message consumedMessage = this.messagesConsumed.poll();
            System.out.printf(MSG_TEMPLATE, consumedMessage, messageConsuming.getName(), this.messagesConsumed.size() + 1);
            super.notifyAll();
            return ofNullable(consumedMessage);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private boolean isShouldConsume (final MessageConsuming consuming) {
        return !this.messagesConsumed.isEmpty()
                && this.messagesConsumed.size() >= consuming.getMinimalMSGAmountToStart();
    }

    private boolean isShouldProduce(final MessageProducingTask messageProducingTask) {
        return this.messagesConsumed.size() < this.maxMessages
                && this.messagesConsumed.size() <= messageProducingTask.getMinimumMSGToStartProduce();
    }
}
