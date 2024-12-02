import static java.lang.Thread.sleep;
import static java.util.stream.IntStream.rangeClosed;
import static java.lang.Thread.currentThread;

public class GemProgression {
    private static final int FIRST_START = 1;
    private static final int FIRST_END = 500;
    private static final int SECOND_START = 501;
    private static final int SECOND_END = 1000;
    private static final String MESSAGE_TEMPLATE = "%s : %d\n";
    private static final int WAITING = 1000;

    public static void main(String[] args) throws InterruptedException {
        final TaskSummingNumbers firstTask = startSubTask(FIRST_START, FIRST_END);
        final TaskSummingNumbers secondTask = startSubTask(SECOND_START, SECOND_END);

        waitForThreads();
        final int resultNumber = firstTask.getResultNumber() + secondTask.getResultNumber();
        printThreadNameAndNumber(resultNumber);
    }

    private static void waitForThreads() throws InterruptedException {
        sleep(WAITING);
    }

    private static void printThreadNameAndNumber(final int number) {
        System.out.printf(MESSAGE_TEMPLATE, Thread.currentThread().getName(), number);
    }

    private static TaskSummingNumbers startSubTask(final int fromNumber, final int toNumber) {
        final TaskSummingNumbers subTask = new TaskSummingNumbers(fromNumber, toNumber);
        final Thread thread = new Thread(subTask);
        thread.start();
        return subTask;
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
