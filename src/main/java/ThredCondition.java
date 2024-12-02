import java.util.regex.Pattern;

public class ThredCondition {
    private static final String MSG_TEMPLATE = "%s : %s\n";
    private static final int WAITING = 1000;
    private static final int JOIN_WAITING = 2000;

    public static void main(String[] args) throws InterruptedException {
        final Thread mainThread = Thread.currentThread();
        final Thread thread = new Thread(() -> {
            try {
                //mainThread.join();
                mainThread.join(JOIN_WAITING);
                showThreadState(Thread.currentThread());
            } catch (final InterruptedException interruptedException) {

            }
        });
        showThreadState(thread);
        thread.start();
        Thread.sleep(WAITING);
        showThreadState(thread);
    }

    private static void showThreadState(final Thread thread) {
        System.out.printf(MSG_TEMPLATE, thread.getName(), thread.getState());
    }
}
