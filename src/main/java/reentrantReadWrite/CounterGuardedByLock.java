package reentrantReadWrite;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterGuardedByLock extends AbstractCounter{
    private final Lock lock = new ReentrantLock();

    @Override
    protected Lock getReadLock() {
        return null;
    }

    @Override
    protected Lock getWriteLock() {
        return null;
    }
}
