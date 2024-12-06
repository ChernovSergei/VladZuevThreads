package messageTransport;

import messageTransport.broker.MessageBroker;
import messageTransport.consumer.MessageConsuming;
import messageTransport.producer.MessageProducingTask;

public class Runner {
    public static void main(String[] args) {
        final int borkerMaxMSGs = 5;
        final MessageBroker messageBroker = new MessageBroker(borkerMaxMSGs);

        final Thread producingThread = new Thread(new MessageProducingTask(messageBroker));
        final Thread consumingThread = new Thread(new MessageConsuming(messageBroker));

        producingThread.start();
        consumingThread.start();
    }
}
