import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ConditionBoundedBuffer<T> {
    private final T[] elements;
    private int size;
    private Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    public ConditionBoundedBuffer(final int capacity) {
        this.elements = (T[]) new Object[capacity];
        this.lock = new ReentrantLock();
        this.notFull = this.lock.newCondition();
        this.notEmpty = this.lock.newCondition();
    }

    public boolean isFull() {
        this.lock.lock();
        try {
            return this.size == this.elements.length;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isEmpty() {
        this.lock.lock();
        try {
            return this.size == 0;
        } finally {
            this.lock.unlock();
        }
    }

    public void put(final T element) {
        this.lock.lock();
        try {
            while (this.isFull()) {
                this.notFull.await();
            }
            this.elements[this.size] = element;
            this.size++;
            System.out.printf("%s was put in buffer. Buffer result %s%n", element, this);
            this.notEmpty.signal();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            this.lock.unlock();
        }
    }

    public T take() {
        this.lock.lock();
        try {
            while (this.isEmpty()) {
                this.notEmpty.await();
            }
            final T result = this.elements[this.size - 1];
            this.elements[this.size - 1] = null;
            this.size--;
            System.out.printf("%s was taken from buffer. Buffer result %s%n", result, this);
            this.notFull.signal();
            return result;
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e );
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public String toString() {
        this.lock.lock();
        try {
            return "{" + Arrays.stream(this.elements, 0, this.size).map(Objects::toString).collect(joining(", ")) + "}";
        } finally {
            this.lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ConditionBoundedBuffer<Integer> boundedBuffer = new ConditionBoundedBuffer<>(5);

        final Runnable producingTask = () -> Stream.iterate(0, i -> i + 1).forEach(i -> {
            try {
                boundedBuffer.put(i);
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        final Thread producingThread = new Thread(producingTask);

        final Runnable consumingTask = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    boundedBuffer.take();
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        final Thread consumingThread = new Thread(consumingTask);

        producingThread.start();
        consumingThread.start();
    }
}