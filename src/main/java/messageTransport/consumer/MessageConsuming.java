package messageTransport.consumer;

import messageTransport.broker.MessageBroker;
import messageTransport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsuming implements Runnable {
    private final MessageBroker messageBroker;
    private static final int SLEEP = 1;
    private final int minimalMSGAmountToStart;
    private final String name;

    public MessageConsuming(MessageBroker messageBroker, int minimalMSGAmountToStart, final String name) {
        this.messageBroker = messageBroker;
        this.minimalMSGAmountToStart = minimalMSGAmountToStart;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMinimalMSGAmountToStart() {
        return minimalMSGAmountToStart;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SLEEP);
                final Optional<Message> consumedMessage = this.messageBroker.consume(this);
                consumedMessage.orElseThrow(MessageConsumingException::new);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}