import java.util.concurrent.ThreadFactory;

public class ThreadUncoughtException {
    private static final String MSG_TEMPLATE = "Exception was thrown with message '%s' in thread '%s'.\n";

    public static void main(String[] args) throws InterruptedException {
       final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (thread, exception)
        -> System.out.printf(MSG_TEMPLATE, exception.getMessage(), thread.getName());
       final ThreadFactory threadFactory = new ThreadWithHandlerFactory(uncaughtExceptionHandler);

       final Thread firstThread = threadFactory.newThread(new Task());
       final Thread secondThread = threadFactory.newThread(new Task());

       firstThread.start();
       secondThread.start();

       firstThread.join();
       secondThread.join();
    }

    public static final class Task implements Runnable {

        private static final String EXCEPTION_MSG = "I am exception";

        @Override
        public void run() {
            System.out.println(Thread.currentThread().isDaemon());
            throw new RuntimeException(EXCEPTION_MSG);
        }
    }

    private static final class ThreadWithHandlerFactory implements ThreadFactory {

        private final Thread.UncaughtExceptionHandler uncoughtException;

        private ThreadWithHandlerFactory(Thread.UncaughtExceptionHandler uncoughtException) {
            this.uncoughtException = uncoughtException;
        }

        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }


}
