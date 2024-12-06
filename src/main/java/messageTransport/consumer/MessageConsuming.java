package messageTransport.consumer;

import messageTransport.broker.MessageBroker;
import messageTransport.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageConsuming implements Runnable {
    private final MessageBroker messageBroker;
    private static final int SLEEP = 1;
    private static final String MSG_TEMPLATE = "Message '%s' is consumed.\n";

    public MessageConsuming(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(SLEEP);
                final Message consumedMessage = this.messageBroker.consume();
                System.out.printf(MSG_TEMPLATE, consumedMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
