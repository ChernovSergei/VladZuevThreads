package messageTransport.producer;

import messageTransport.broker.MessageBroker;
import messageTransport.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask implements Runnable {
    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private static final int SLEEP = 1;
    private int minimumMSGToStartProduce;
    private final String name;

    public MessageProducingTask(MessageBroker messageBroker, final MessageFactory messageFactory, final int minimumMSGToStartProduce, final String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.minimumMSGToStartProduce = minimumMSGToStartProduce;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMinimumMSGToStartProduce() {
        return minimumMSGToStartProduce;
    }

    public void setMinimumMSGToStartProduce(int minimumMSGToStartProduce) {
        this.minimumMSGToStartProduce = minimumMSGToStartProduce;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                final Message producedMSG = this.messageFactory.create();
                TimeUnit.SECONDS.sleep(SLEEP);
                this.messageBroker.produce(producedMSG, this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
