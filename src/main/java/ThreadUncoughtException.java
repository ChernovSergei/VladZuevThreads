import static java.lang.Thread.setDefaultUncaughtExceptionHandler;

public class ThreadUncoughtException {
    private static final String MSG_TEMPLATE = "Exception was thrown with message '%s' in thread '%s'.\n";

    public static void main(String[] args) {
       final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (thread, exception)
        -> System.out.printf(MSG_TEMPLATE, exception.getMessage(), thread.getName());
       final Thread firstThread = new Thread(new Task());
       final Thread secondThread = new Thread(new Task());

       setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

       /*firstThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
       secondThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);*/

       firstThread.start();
       secondThread.start();
    }

    public static final class Task implements Runnable {

        private static final String EXCEPTION_MSG = "I am exception";

        @Override
        public void run() {
            throw new RuntimeException(EXCEPTION_MSG);
        }
    }
}
