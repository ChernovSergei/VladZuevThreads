import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;

public class ThreadBranches {
    public static void main(String[] args) {
        final Runnable displyThreadName = () -> System.out.println(currentThread().getName());
        final Runnable createThreads = () ->
                range(0, 10).forEach(i -> startThread(displyThreadName));
        startThread(createThreads);
    }

    private static void startThread(final Runnable runnable) {
        final Thread thread = new Thread(runnable);
        thread.start();
    }
}
