package messageTransport.producer;

import messageTransport.broker.MessageBroker;
import messageTransport.model.Message;

import java.util.concurrent.TimeUnit;

public class MessageProducingTask implements Runnable {
    private static final String MSG_PRODUCED = "Message '%s' is produced.\n";
    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private static final int SLEEP = 1;

    public MessageProducingTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        this.messageFactory = new MessageFactory();
    }

    private static final class MessageFactory {
        private static final int INITIAL_INDEX = 1;
        private static final String TEMPLATE_MSG_DATA = "Message#%d";
        private int nextMessage;

        public MessageFactory() {
            this.nextMessage = INITIAL_INDEX;
        }

        public Message create() {
            return new Message(String.format(TEMPLATE_MSG_DATA, this.nextMessage++));
        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                final Message producedMSG = this.messageFactory.create();
                TimeUnit.SECONDS.sleep(SLEEP);
                this.messageBroker.produce(producedMSG);
                System.out.printf(MSG_PRODUCED, producedMSG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }
}
