import java.util.stream.IntStream;

public class ThreadPriorityCheck {
    private static final String MSG_THRD_COMPLETED = "Main thread completed";
    private static String FORMAT = "%s : %s;\n";

    public static void main(String[] args) {
        final Thread threadHigh = new Thread(new Task());
        final Thread threadLow = new Thread(new Task());
        threadHigh.setPriority(Thread.MAX_PRIORITY);
        threadHigh.start();
        threadLow.setPriority(Thread.MIN_PRIORITY);
        threadLow.start();
        System.out.println(MSG_THRD_COMPLETED);
    }

    private static final class Task implements Runnable {
        private static final int RANG_MIN = 0;
        private static final int RANG_MAX = 100;

        @Override
        public void run() {
            IntStream.range(RANG_MIN, RANG_MAX).forEach( e -> {
                System.out.printf(FORMAT, Thread.currentThread().getName(), e);
            });
        }
    }
}
