package messageTransport.producer;

import messageTransport.broker.MessageBroker;
import messageTransport.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask implements Runnable {
    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private static final int SLEEP = 1;

    public MessageProducingTask(MessageBroker messageBroker, final MessageFactory messageFactory) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                final Message producedMSG = this.messageFactory.create();
                TimeUnit.SECONDS.sleep(SLEEP);
                this.messageBroker.produce(producedMSG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }
}
