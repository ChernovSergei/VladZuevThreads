package messageTransport;

import messageTransport.broker.MessageBroker;
import messageTransport.consumer.MessageConsuming;
import messageTransport.producer.MessageFactory;
import messageTransport.producer.MessageProducingTask;

import static java.util.Arrays.stream;

public class Runner {
    public static void main(String[] args) {
        final int borkerMaxMSGs = 1;
        final MessageBroker messageBroker = new MessageBroker(borkerMaxMSGs);

        final MessageFactory messageFactory = new MessageFactory();

        final Thread producingThread1 = new Thread(new MessageProducingTask(messageBroker, messageFactory,
                borkerMaxMSGs, "Producer 1"));
        final Thread producingThread2 = new Thread(new MessageProducingTask(messageBroker, messageFactory,
                10, "Producer 2"));
        final Thread consumingThread1 = new Thread(new MessageConsuming(messageBroker,
                0, "Consumer 1"));

        startThreads(producingThread1, producingThread2, consumingThread1);
    }

    private static void startThreads(final Thread... threads) {
        stream(threads).forEach(Thread::start);
    }
}
