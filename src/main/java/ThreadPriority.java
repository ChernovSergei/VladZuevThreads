public class ThreadPriority {
    private static final String MSG_THREAD = "%s : %d\n";

    public static void main(String[] args) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        final Thread thread = new Thread(() -> printNamePriority(Thread.currentThread()));
        thread.start();
        printNamePriority(Thread.currentThread());
    }

    private static void printNamePriority(final Thread thread) {
        System.out.printf(MSG_THREAD, thread.getName(), thread.getPriority());
    }
}
