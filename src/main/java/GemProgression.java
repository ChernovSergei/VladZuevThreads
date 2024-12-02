import static java.lang.Thread.sleep;
import static java.util.stream.IntStream.rangeClosed;
import static java.lang.Thread.currentThread;

public class GemProgression {
    private static final int FIRST_START = 1;
    private static final int FIRST_END = 500;
    private static final int SECOND_START = 501;
    private static final int SECOND_END = 1000;
    private static final String MESSAGE_TEMPLATE = "%s : %d\n";

    public static void main(String[] args) throws InterruptedException {
        final TaskSummingNumbers firstTask = new TaskSummingNumbers(FIRST_START, FIRST_END);
        final TaskSummingNumbers secondTask = new TaskSummingNumbers(SECOND_START, SECOND_END);
        final Thread firstThread = new Thread(firstTask);
        final Thread secondThread = new Thread(secondTask);
        firstThread.start();
        secondThread.start();

        waitForThreads(firstThread, secondThread);
        final int resultNumber = firstTask.getResultNumber() + secondTask.getResultNumber();
        printThreadNameAndNumber(resultNumber);
    }

    private static void waitForThreads(Thread... threads) throws InterruptedException {
        for (final Thread thread : threads) {
            thread.join();
        }
    }

    private static void printThreadNameAndNumber(final int number) {
        System.out.printf(MESSAGE_TEMPLATE, Thread.currentThread().getName(), number);
    }

    private static final class TaskSummingNumbers implements Runnable {
        private static final int INITIAL_VALUE_RESULT = 0;
        private final int fromNumber;
        private final int toNumber;
        private int resultNumber;

        public TaskSummingNumbers(final int fromNumber, final int toNumber) {
            this.fromNumber = fromNumber;
            this.toNumber = toNumber;
            this.resultNumber = INITIAL_VALUE_RESULT;
        }

        public int getResultNumber() {
            return this.resultNumber;
        }

        @Override
        public void run() {
            rangeClosed(this.fromNumber, this.toNumber).forEach(i -> this.resultNumber += i);
            printThreadNameAndNumber(this.resultNumber);
        }
    }
}
