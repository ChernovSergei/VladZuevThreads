public class ThreadDaemonInheritnance {
    private static final String MSG_DAEMON_FORMAT = "%s : %b\n";

    public static void main (String[] args) throws InterruptedException {
        final Thread firstThread = new Thread (() -> {
            try {
                printThreadNameAndStatus(Thread.currentThread());
                final Thread secondThread = new Thread(() -> printThreadNameAndStatus(Thread.currentThread()));
                secondThread.start();
                secondThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        firstThread.setDaemon(true);
        firstThread.start();
        firstThread.join();
    }

    private static void printThreadNameAndStatus(final Thread thread) {
        System.out.printf(MSG_DAEMON_FORMAT, thread.getName(), thread.isDaemon());
    }
}
