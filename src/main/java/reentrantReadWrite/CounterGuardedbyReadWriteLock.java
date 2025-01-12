package reentrantReadWrite;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CounterGuardedbyReadWriteLock extends AbstractCounter{
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = this.lock.readLock();
    private final Lock writeLock = this.lock.writeLock();

    @Override
    protected Lock getReadLock() {
        return this.readLock;
    }

    @Override
    protected Lock getWriteLock() {
        return this.writeLock;
    }
}
