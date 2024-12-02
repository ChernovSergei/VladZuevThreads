import static java.lang.Thread.currentThread;

public class Runner {

    public static void main(String[] args) {
        System.out.println(currentThread().getName());

        final Thread thread0 = new MyThread();

        final Thread thread1 = new Thread() {
            @Override
            public void run() {
                System.out.println(currentThread().getName());
            }
        };

        final Runnable task = () -> System.out.println(currentThread().getName());

        final Thread thread2 = new Thread(task);

        thread0.run();
        thread0.start();
        thread1.start();
        thread2.start();
    }

    private static final class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(currentThread().getName());
        }
    }
}
