public class ThreadConditionTerminated {
    private static final String MSG_TEMPLATE = "%s : %s\n";

    public static void main(String[] args) throws InterruptedException {
        final Thread thread = new Thread(() -> {
            throw new RuntimeException();
        });
        showThreadState(thread);
        thread.start();
        showThreadState(thread);
        thread.join();
        showThreadState(thread);
    }

    private static void showThreadState(final Thread thread) {
        System.out.printf(MSG_TEMPLATE, thread.getName(), thread.getState());
    }
}
