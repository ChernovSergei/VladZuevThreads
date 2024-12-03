import java.util.concurrent.TimeUnit;

public class ThreadDeamon {
    private static final String MSG_COMPLETED = "Main thread is completed";
    public static void main(String[] args) {
        final Thread thread = new Thread(new Task());
        thread.start();
        System.out.println(thread.isDaemon());
        System.out.println(MSG_COMPLETED);
    }

    private static final class Task implements Runnable {
        private static final String MSG = "I am working";
        private static final int DURATION = 2;

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(MSG);
                    TimeUnit.SECONDS.sleep(DURATION);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
