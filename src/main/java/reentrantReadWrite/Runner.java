package reentrantReadWrite;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

public class Runner {

    public static void main(String[] args) throws InterruptedException{
        testCounter(CounterGuardedByLock::new);
    }

    private static void testCounter(final Supplier<? extends AbstractCounter> counterFactory) throws InterruptedException {
        final AbstractCounter counter = counterFactory.get();
        final int amountOfThreadsGettingValue = 50;
        final ReadingValueTask[] readingValueTasks = createReadingTask(counter, amountOfThreadsGettingValue);
        final Thread[] readingValueThreads = mapToThreads(readingValueTasks);
        final Runnable incrementingCounterTask = createIncrementingCounterTask(counter);
        final int amountOfThreadsIncrementingCounter = 2;
        final Thread[] incrementingCounterThreads = createThreads(incrementingCounterTask, amountOfThreadsIncrementingCounter);
        startThread(readingValueThreads);
        startThread(incrementingCounterThreads);
        TimeUnit.SECONDS.sleep(5);
        interruptThreads(readingValueThreads);
        interruptThreads(incrementingCounterThreads);
        waitUntilFinish(readingValueThreads);
        final long totalAmountOfThreads = findTotalAmountOfReads(readingValueTasks);
        System.out.println(totalAmountOfThreads);
    }

    private static Runnable createIncrementingCounterTask(final AbstractCounter counter) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                incrementCounter(counter);
            }
        };
    }

    private static void incrementCounter(final AbstractCounter counter) {
        try {
            counter.increment();
            TimeUnit.SECONDS.sleep(1);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static Thread[] createThreads(final Runnable task, final int amountOfThreads) {
        return IntStream.range(0, amountOfThreads)
                .mapToObj(i -> new Thread(task))
                .toArray(Thread[]::new);
    }

    private static void startThread(final Thread[] threads) {
        forEach(threads, Thread::start);
    }

    private static void forEach(final Thread[] threads, final Consumer<Thread> action) {
        stream(threads).forEach(action);
    }

    private static void waitUntilFinish(final Thread[] threads) {
        forEach(threads, Runner::waitUntilFinish);
    }

    private static void waitUntilFinish(final Thread thread) {
        try {
            thread.join();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static long findTotalAmountOfReads(final ReadingValueTask[] tasks) {
        return stream(tasks)
                .mapToLong(ReadingValueTask::getAmountOfReads)
                .sum();
    }

    private static ReadingValueTask[] createReadingTask(final AbstractCounter counter, final int amounterOfTasks) {
        return IntStream.range(0, amounterOfTasks).mapToObj(i -> new ReadingValueTask(counter))
                .toArray(ReadingValueTask[]::new);
    }

    private static Thread[] mapToThreads(final Runnable[] tasks) {
        return stream(tasks)
                .map(Thread::new)
                .toArray(Thread[]::new);
    }

    private static void interruptThreads(final Thread[] threads) {
        forEach(threads, Thread::interrupt);
    }

    private static final class ReadingValueTask implements Runnable {
        private final AbstractCounter counter;
        private long amountOfReads;

        public ReadingValueTask(final AbstractCounter counter) {
            this.counter = counter;
        }

        public long getAmountOfReads() {
            return amountOfReads;
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                this.counter.getValue();
                this.amountOfReads++;
            }
        }
    }
}
