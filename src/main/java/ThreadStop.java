import java.util.concurrent.TimeUnit;

public class ThreadStop {
    private static final String MSG_SENT = "\nRequest was sent.";
    private static final int DURATION = 1;
    private static final String MSG_RESPONSE = "Response was received.";
    private static final String MSG_STOPPED = "Server is stopped.";
    private static final int WAITING = 5;

    public static void main(String[] args) throws InterruptedException {
        final Thread thread = new Thread(() ->  {
            try {
                while (true) {
                    doRequest();
                }
            } catch (final InterruptedException e) {

            }
        });
        thread.start();

        final Thread stoppingThread = new Thread(() -> {
            if (isServerStopped()) {
                thread.stop();
                stopServer();
            }
        });
        TimeUnit.SECONDS.sleep(WAITING);
        stoppingThread.start();
    }

    private static void doRequest() throws InterruptedException{
        System.out.println(MSG_SENT);
        TimeUnit.SECONDS.sleep(DURATION);
        System.out.println(MSG_RESPONSE);
    }

    private static boolean isServerStopped() {
        return true;
    }

    private static void stopServer() {
        System.out.println(MSG_STOPPED);
    }
}
