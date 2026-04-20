package com.solvd.autoservicerepair.threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Thread-safe, singleton Connection Pool.
 * - Singleton: double-checked locking with volatile instance field.
 * - Lazy initialization: pool is not created until getInstance() is first called.
 * - Thread-safety: BlockingQueue (ArrayBlockingQueue) handles concurrent access.
 * getConnection() BLOCKS the calling thread until a connection is available —
 * threads never busy-wait; they simply park until released.
 * - releaseConnection() returns the connection back to the queue.
 */
@Slf4j
public class ConnectionPool {

    // volatile ensures visibility of the reference across threads (no stale read)
    private static volatile ConnectionPool instance;

    private final BlockingQueue<AccountDao> pool;

    /**
     * Private constructor — accepts the desired pool size.
     * Fills the BlockingQueue with fresh AccountDao connections.
     */
    private ConnectionPool(int size) {
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 1; i <= size; i++) {
            pool.offer(new AccountDao(i));
        }
        log.info("ConnectionPool initialized with {} connections.", size);
    }

    /**
     * Lazy singleton with double-checked locking.
     * First call creates the pool; subsequent calls return the same instance.
     */
    public static ConnectionPool getInstance(int size) {
        if (instance == null) {                         // first check (no sync, fast path)
            synchronized (ConnectionPool.class) {
                if (instance == null) {                 // second check (inside lock, safe)
                    instance = new ConnectionPool(size);
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves a connection from the pool.
     * BLOCKS the calling thread if no connection is available,
     * resuming automatically once releaseConnection() puts one back.
     */
    public AccountDao getConnection() throws InterruptedException {
        String thread = Thread.currentThread().getName();
        log.info("[{}] Waiting for a connection... (available: {})", thread, pool.size());
        AccountDao connection = pool.take();   // blocks until available
        log.info("[{}] Acquired {}", thread, connection);
        return connection;
    }

    /**
     * Returns a connection back to the pool, unblocking any waiting thread.
     */
    public void releaseConnection(AccountDao connection) {
        String thread = Thread.currentThread().getName();
        pool.offer(connection);
        log.info("[{}] Released {} (available: {})", thread, connection, pool.size());
    }

    /**
     * For diagnostics only.
     */
    public int availableConnections() {
        return pool.size();
    }

}
