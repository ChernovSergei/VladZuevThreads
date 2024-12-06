import java.util.stream.IntStream;

public class ThreadChasing {
    private static int counter = 0;
    private static final int INCREMENT_AMOUNT_FIRST = 500;
    private static final int INCREMENT_AMOUNT_SECOND = 600;

    public static void main(String[] args) throws InterruptedException {
        final Thread firstThread = createIncrementCounterThread(INCREMENT_AMOUNT_FIRST);
        final Thread secondThread = createIncrementCounterThread(INCREMENT_AMOUNT_SECOND);

        firstThread.start();
        secondThread.start();

        firstThread.join();
        secondThread.join();

        System.out.println(counter);
    }

    private static Thread createIncrementCounterThread(final int increment) {
        return new Thread(() -> IntStream.range(0, increment).forEach(i -> counter++));
    }
}
