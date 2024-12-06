import java.io.FileReader;
import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ThreadSynchronized {
    private static int firstCounter = 0;
    private static int secondCounter = 0;
    private static final int INCREMENT_AMOUNT_FIRST = 500;
    private static final int INCREMENT_AMOUNT_SECOND = 600;
    private final static Object LOCK_FIRST = new Object();
    private final static Object LOCK_SECOND = new Object();

    public static void main(String[] args) throws InterruptedException {

        final Thread firstThread = createIncrementCounterThread(INCREMENT_AMOUNT_FIRST,  i -> incrementFirstCounter());
        final Thread secondThread = createIncrementCounterThread(INCREMENT_AMOUNT_FIRST,  i -> incrementFirstCounter());
        final Thread thirdThread = createIncrementCounterThread(INCREMENT_AMOUNT_SECOND, i -> incrementSecondCounter());
        final Thread fourthThread = createIncrementCounterThread(INCREMENT_AMOUNT_SECOND, i -> incrementSecondCounter());

        startThreads(firstThread, secondThread, thirdThread, fourthThread);

        joinThreads(firstThread, secondThread, thirdThread, fourthThread);

        System.out.println(firstCounter);
        System.out.println(secondCounter);
    }

    private static Thread createIncrementCounterThread(final int increment, final IntConsumer incrementOperation) {
        return new Thread(() -> IntStream.range(0, increment).forEach(incrementOperation));
    }

    private static void startThreads(final Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static void joinThreads(final Thread... threads) {
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e);
                Thread.currentThread().interrupt();
            }
        });
    }

    private static void incrementFirstCounter() {
        synchronized (LOCK_FIRST) {
            firstCounter++;
        }
    }

    private static void incrementSecondCounter() {
        synchronized (LOCK_SECOND) {
            secondCounter++;
        }
    }
}
