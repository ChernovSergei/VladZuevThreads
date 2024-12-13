package messageTransport.consumer;

import messageTransport.broker.MessageBroker;
import messageTransport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsuming implements Runnable {
    private final MessageBroker messageBroker;
    private static final int SLEEP = 1;

    public MessageConsuming(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SLEEP);
                final Optional<Message> consumedMessage = this.messageBroker.consume();
                consumedMessage.orElseThrow(MessageConsumingException::new);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}