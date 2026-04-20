package com.solvd.autoservicerepair.threads;

import com.solvd.autoservicerepair.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Part 1:
 * - Thread created via Runnable interface (passed into new Thread())
 * - Thread created via Thread subclass (extends Thread)
 * Part 2:
 * - ConnectionPool: singleton, lazy-initialized, thread-safe with BlockingQueue
 * - AccountDao as the mocked Connection class
 * Part 3:
 * - Pool size = 5
 * - 7 threads submitted via ExecutorService (newFixedThreadPool(7))
 * - First 5 threads have connection immediately and HOLD them for 5 seconds
 * - Threads 6 and 7 wait
 * - The main program also waits, it does not exit until all 7 finish
 * - After 5 seconds, connections start releasing, threads 6 & 7 unblock and proceed
 */

@Slf4j
public class ThreadDemo {

    private static final int POOL_SIZE = 5;
    private static final int THREAD_COUNT = 7;
    // First 5 threads hold their connection this long so threads 6 & 7 are visibly stuck waiting
    private static final int HOLD_SECONDS = 5000;

    public static void main(String[] args) throws InterruptedException {

        /* Part 1.1 - Thread via Runnable */
        log.info("--------------------------------------------------------------");
        log.info("Part 1.1 - Thread created via implemented Runnable interface");
        log.info("--------------------------------------------------------------");

        Runnable garageRunnable = new GarageStatusRunnable("AutoFix Tbilisi", 3);
        Thread runnableThread = new Thread(garageRunnable, "RunnableThread-Garage");
        runnableThread.start();
        runnableThread.join();

        /* Part 1.2 - Thread via extended Thread subclass */
        log.info("--------------------------------------------------------------");
        log.info("Part 1.2 - Thread created via extended Thread class");
        log.info("--------------------------------------------------------------");

        MechanicAssignmentThread mechanicThread =
                new MechanicAssignmentThread("Nika", "Oil Change");
        mechanicThread.start();
        mechanicThread.join();

        /* Part 2 & 3 - Connection Pool + ExecutorService */
        log.info("--------------------------------------------------------------");
        log.info("Part 2 & 3 — ConnectionPool + ExecutorService");
        log.info("Pool size: {}  |  Threads: {}", POOL_SIZE, THREAD_COUNT);
        log.info("--------------------------------------------------------------");

        // lazy singleton - pool is created here for the first and only time.
        ConnectionPool pool = ConnectionPool.getInstance(POOL_SIZE);

        // CountDownLatch makes all 7 threads race to getConnection() at the same
        // instant so the "2 threads waiting" effect visible in the logs.
        CountDownLatch startSignal = new CountDownLatch(1);

        // ExecutorService caps at exactly THREAD_COUNT (7) threads.
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String thread = Thread.currentThread().getName();
                AccountDao connection = null;
                try {
                    // All 7 threads wait here until the main gives signal.
                    startSignal.await();

                    log.info("[{}] Task-{} → calling getConnection()...", thread, taskId);

                    // *** BlockingQueue.take() - BLOCKS if pool exhausted ***
                    // Threads 6 and 7 will block here until one of the first 5
                    // threads calls releaseConnection().
                    connection = pool.getConnection();

                    // Use the connection (mocked DB operations)
                    connection.create("Customer-" + taskId);
                    connection.get(taskId);
                    connection.update(taskId, "UpdatedCustomer-" + taskId);
                    connection.delete(taskId);

                    if (taskId <= POOL_SIZE) {
                        // First 5 threads intentionally hold the connection so
                        // threads 6 & 7 are forced to wait visibly.
                        log.info("[{}] Task-{} holding connection for {} seconds (threads 6 & 7 are now waiting)...",
                                thread, taskId, HOLD_SECONDS);
                        Thread.sleep(HOLD_SECONDS);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("[{}] Task-{} interrupted.", thread, taskId);
                } finally {
                    if (connection != null) {
                        // releaseConnection() - 1 thread place is cleared for awaiting thread
                        pool.releaseConnection(connection);
                    }
                }
            });
        }

        // Signal all 7 threads race to getConnection() simultaneously.
        log.info("Signal for all {} threads to getConnection() at once!", THREAD_COUNT);
        startSignal.countDown();

        // The PROGRAM itself waits here - it will not exit until all 7 tasks
        // complete, including threads 6 & 7 which had to wait for a free connection.

        executor.shutdown();
        boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);

        if (finished) {
            log.info("--------------------------------------------------------------");
            log.info("All {} tasks completed.", THREAD_COUNT);
            log.info("Connections back in pool: {}", pool.availableConnections());
            log.info("--------------------------------------------------------------");
        } else {
            log.warn("Executor timed out — not all tasks finished within 60 seconds.");
        }

        log.info("--------------------------------------------------------------");
        log.info("Part 4 — CompletableFuture examples");
        log.info("--------------------------------------------------------------");

        ExecutorService completableFutureExecutor = Executors.newFixedThreadPool(4);

        cf1_manualCompletion(pool);
        cf2_thenApply(completableFutureExecutor, pool);
        cf3_thenAccept_thenRun(completableFutureExecutor, pool);
        cf4_thenCombine(completableFutureExecutor, pool);
        cf5_allOf_exceptionally(completableFutureExecutor, pool);

        completableFutureExecutor.shutdown();
        boolean cfFinished = completableFutureExecutor.awaitTermination(30, TimeUnit.SECONDS);
        if (!cfFinished) {
            log.warn("CF executor timed out — some CompletableFuture tasks may still be running.");
        }

        log.info("--------------------------------------------------------------");
        log.info("ALL PARTS COMPLETE.");
        log.info("--------------------------------------------------------------");
    }

    /* --------------------------------------------------------------
       CF EXAMPLE 1 — Manual completion — WITHOUT CompletionStage

       This shows the most basic way to use CompletableFuture as a simple "promise" container.


       Real scenario: a mechanic thread finishes reading an account and
       manually fires the result into a future the main thread is waiting on.
       -------------------------------------------------------------- */
    static void cf1_manualCompletion(ConnectionPool pool) {

        log.info("--- CF1: Manual completion (WITHOUT CompletionStage) ---");

        // Empty box that will hold future string
        CompletableFuture<String> promise = new CompletableFuture<>();

        // Separate thread that will do work independently - will put string in box

        Thread worker = new Thread(() -> {
            try {
                AccountDao connection = pool.getConnection();       // borrow from pool
                String result = connection.get(1);                  // fake DB read
                pool.releaseConnection(connection);                 // return to pool
                SleepUtils.sleep(500);                   // simulate extra work (like paperwork)

                // puts string in box and wakes up anyone waiting
                promise.complete(result);
                log.info("[{}] CF1: promise.complete() called.", Thread.currentThread().getName());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                // completeExceptionally() puts an exception in instead of a value.
                promise.completeExceptionally(e);
            }
        }, "CF1-ManualThread");

        worker.start(); //  Launches the worker thread so it starts working.


         /* Blocks (pauses) the main thread until
        the promise has something in it (either a value or an error).
        This is like waiting at the mailbox for the letter to arrive. */

        String value = promise.join();

        log.info("[Main] CF1 received: {}", value);

        try {
            worker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        /* What this teaches: The simplest pattern — create an empty future,
        manually complete it later, and block waiting for it. */
    }

    /* --------------------------------------------------------------
       CF EXAMPLE 2 — thenApply() pipeline — WITH CompletionStage

       This shows chaining transformations automatically without manual waiting between steps

       Real scenario: fetch an account → parse the balance out of the string
       - format a display message. Three stages, zero manual waiting.
       --------------------------------------------------------------*/
    static void cf2_thenApply(ExecutorService executor, ConnectionPool pool) {

        log.info("--- CF2: thenApply() transform pipeline (WITH CompletionStage) ---");

        // Stage 1 - fetchStage - supplyAsync: submit a task to the executor, get a future back.
        // The lambda runs on an executor thread; main thread is free immediately.
        // Return type: CompletableFuture<String>  (the raw account string from the DB)

        CompletableFuture<String> fetchStage = CompletableFuture.supplyAsync(() -> {

            log.info("[{}] CF2 Stage1: fetching account...", Thread.currentThread().getName());

            try {
                AccountDao connection = pool.getConnection();
                String result = connection.get(2);
                pool.releaseConnection(connection);
                return result;                          // this String flows into Stage 2
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            }
        }, executor);

        // Stage 2 - thenApply (parseStage): When the previous stage finishes,
        // take its result (accountData String), transform it into something else (a Double balance),
        // and return a new future containing that Double. This runs automatically - no manual triggering.
        // Input: String (account data)   Output: Double (balance)

        CompletableFuture<Double> parseStage = fetchStage.thenApply(accountData -> {

            log.info("[{}] CF2 Stage2: parsing balance from: {}", Thread.currentThread().getName(), accountData);

            return 2500.00;                             // Double flows into Stage 3

        });

        // Stage 3 — thenApply again: Another automatic transformation - takes the Double balance,
        // formats it into a readable String message.
        // Input: Double (balance)   Output: String (formatted message)
        CompletableFuture<String> formatStage = parseStage.thenApply(balance -> {

            log.info("[{}] CF2 Stage3: formatting message...", Thread.currentThread().getName());

            return String.format("Account balance: %.2f GEL",
                    balance);
        });

        // join() — wait for the entire pipeline and get the final String.
        // No blocking happened between stages; only this one final wait.
        log.info("[Main] CF2 result: {}", formatStage.join());

        /* What this teaches: You can chain transformations (thenApply) that automatically trigger
            when previous work finishes. No manual waiting between steps. */
    }

    /* --------------------------------------------------------------
       CF EXAMPLE 3 - thenAccept() + thenRun() - WITH CompletionStage

       This shows consuming results without returning values,
       and running code that doesn't need the result at all.

       thenApply  - receives value, RETURNS new value  (transform)
       thenAccept - receives value, returns VOID        (consume/print/save)
       thenRun    - receives NOTHING, returns VOID      (side effect after)

       Return type narrows with each step:
         CompletableFuture<String> - CompletableFuture<Void> - CompletableFuture<Void>

       Real scenario: create an account - print the confirmation - log audit entry.
       The audit log doesn't need the confirmation text, just needs to run after.
       -------------------------------------------------------------- */
    static void cf3_thenAccept_thenRun(ExecutorService executor, ConnectionPool pool) {

        log.info("--- CF3: thenAccept() + thenRun() (WITH CompletionStage) ---");

        CompletableFuture.supplyAsync(() -> {

                    // Stage 1: Starts a task that creates an account and returns a confirmation message String.

                    log.info("[{}] CF3 Stage1: creating account...", Thread.currentThread().getName());

                    try {
                        AccountDao connection = pool.getConnection();
                        connection.create("NewCustomer-CF3");
                        pool.releaseConnection(connection);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new CompletionException(e);
                    }

                    String confirmation = "Account 'NewCustomer-CF3' created successfully"; // A confirmation String.

                    return confirmation;

                }, executor)

                /*  thenAccept means: "Take the String from the previous stage, use it (consume it),
                but don't return anything new." Like reading a letter and then throwing away the envelope.
                Returns CompletableFuture<Void>. */

                .thenAccept(confirmation -> {

                    log.info("[{}] CF3 Stage2 thenAccept: printing confirmation: {}",

                            Thread.currentThread().getName(), confirmation);
                })

                // thenRun: Void came in (nothing), nothing goes out.

                .thenRun(() -> log.info("[{}] CF3 Stage3 thenRun: audit log entry written — account creation recorded.",
                        Thread.currentThread().getName()))

                .join();    // wait for the full chain to finish

        log.info("[Main] CF3 pipeline finished.");
    }

    /* --------------------------------------------------------------
       CF EXAMPLE 4 — thenCombine() — WITH CompletionStage
       -------------------------------------------------------------------------
       This shows running TWO independent tasks in parallel
       and merging their results when BOTH finish.

       Time saved:
         Sequential: 500ms + 400ms = 900ms
         Parallel:   max(500ms, 400ms) = 500ms

       Real scenario: fetch account details AND fetch account transaction history
       at the same time, then build a full report combining both.
       -------------------------------------------------------------- */
    static void cf4_thenCombine(ExecutorService executor, ConnectionPool pool) {

        log.info("--- CF4: thenCombine() parallel merge (WITH CompletionStage) ---");

        // Starts Future A: fetches account details. Takes 400ms (simulated with sleep)
        CompletableFuture<String> accountFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[{}] CF4 FutureA: fetching account details...", Thread.currentThread().getName());
            try {
                AccountDao conn = pool.getConnection();
                String result = conn.get(3);
                pool.releaseConnection(conn);
                SleepUtils.sleep(400);                         // simulate slower network call
                return result;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            }
        }, executor);

        // Starts Future B: fetches transaction history. Takes 500ms.
        // Both run at the same time on different threads.

        CompletableFuture<String> historyFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[{}] CF4 FutureB: fetching transaction history...", Thread.currentThread().getName());
            SleepUtils.sleep(500);                                // finishes before Future A
            return "Transactions: [OilChange 50GEL, TireChange 100GEL]";
        }, executor);

        // thenCombine: waits for BOTH futures, then calls the BiFunction with both results.
        // T = String (account details from A)
        // U = String (history from B)
        // R = String (combined report)
        String fullReport = accountFuture
                .thenCombine(historyFuture, (accountData, historyData) -> {

                    log.info("[{}] CF4 thenCombine: merging both results...", Thread.currentThread().getName());

                    return "FULL REPORT | " + accountData + " | " + historyData; //  merge both results into a single report string
                })
                .join();

        log.info("[Main] CF4 result: {}", fullReport);

        /* What this teaches: When you have two independent tasks,
        run them simultaneously and merge results when both are ready. */
    }

    /* --------------------------------------------------------------
       CF EXAMPLE 5 — allOf() + exceptionally() — WITH CompletionStage
       -------------------------------------------------------------------------

       This shows waiting for MANY tasks to all complete, plus error recovery (fallback values)

       allOf(cf1, cf2, cf3...): waits for ALL given futures to complete.
         Returns CompletableFuture<Void> — no result carried, retrieve separately.

       exceptionally(Function<Throwable,T>): the async catch block.
         If any stage throws, exceptionally() catches it and returns a fallback
         value of the same type T so the chain continues instead of dying.

       Real scenario: run 3 account operations in parallel (update, delete, get).
       One of them intentionally fails. exceptionally() recovers it gracefully.
       All 3 complete (or fall back), then we print a summary.
       -------------------------------------------------------------- */
    static void cf5_allOf_exceptionally(ExecutorService executor, ConnectionPool pool) {

        log.info("--- CF5: allOf() + exceptionally() (WITH CompletionStage) ---");

        // Operation 1: update account — succeeds normally
        CompletableFuture<String> operation1 = CompletableFuture.supplyAsync(() -> {
                    log.info("[{}] CF5 Op1: updating account...", Thread.currentThread().getName());
                    try {
                        AccountDao connection = pool.getConnection();
                        connection.update(4, "UpdatedName-CF5");
                        pool.releaseConnection(connection);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new CompletionException(e);
                    }
                    SleepUtils.sleep(300);
                    return "Op1: UPDATE — SUCCESS";
                }, executor)
                // exceptionally on operation1: if this stage throws anything,
                // the exception is caught here and the fallback String is used instead.
                // The chain does NOT crash — it continues with the fallback.
                .exceptionally(ex -> {

                    log.warn("[{}] CF5 Op1 failed: {}. Fallback applied.", Thread.currentThread().getName(), ex.getMessage());

                    return "Op1: UPDATE — FAILED (fallback)";
                });

        // Operation 2: intentional failure — demonstrates exceptionally()
        CompletableFuture<String> operation2 = CompletableFuture.supplyAsync(() -> {
                    log.info("[{}] CF5 Op2: deleting account (will fail)...", Thread.currentThread().getName());
                    SleepUtils.sleep(200);
                    // Throwing here simulates a real failure (e.g. DB constraint violation).
                    // The if(Math.random) to stop compiler whining and sometimes exception sometimes not
                    // unreachable code on the return below.
                    if (Math.random() < 0.5) throw new RuntimeException("CF5: delete rejected — account has active orders!");
                    return "Op2: DELETE — SUCCESS";    // never reached
                }, executor)
                // exceptionally catches the RuntimeException thrown above.
                // Returns a fallback String of the same type (String) so allOf() still completes.
                .exceptionally(ex -> {
                    log.warn("[{}] CF5 Op2 failed: '{}'. Fallback applied.", Thread.currentThread().getName(), ex.getMessage());
                    return "Op2: DELETE — FAILED (fallback: account protected)";
                });

        // Operation 3: async get using the new getAsync() on AccountDao — succeeds normally
        CompletableFuture<String> operation3;
        try {
            AccountDao connection = pool.getConnection();
            // getAsync() internally calls supplyAsync — a future returned from within
            // another future. The connection is released inside the lambda after the work.
            operation3 = connection.getAsync(5, executor)
                    .thenApply(result -> {
                        // thenApply chained on top of getAsync's future — release connection
                        // then transform the raw DB result into a labelled summary string.
                        pool.releaseConnection(connection);
                        return "Op3: ASYNC GET — SUCCESS | " + result;
                    })
                    .exceptionally(ex -> {
                        pool.releaseConnection(connection);
                        log.warn("[{}] CF5 Op3 failed: {}", Thread.currentThread().getName(), ex.getMessage());
                        return "Op3: ASYNC GET — FAILED (fallback)";
                    });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // If getConnection() itself was interrupted before we even started,
            // create an already-completed future with a fallback value so allOf() still works.
            operation3 = CompletableFuture.completedFuture("Op3: skipped — interrupted before start");
        }

        // allOf() — takes any number of CompletableFutures (different types are fine
        // because it uses wildcard <?>) and returns CompletableFuture<Void> that
        // completes only when EVERY ONE of op1, op2, op3 has completed (or fallen back).
        // Total wait = max(op1 time, op2 time, op3 time) — all run in parallel.
        CompletableFuture<Void> allOps = CompletableFuture.allOf(operation1, operation2, operation3);

        // This one join() waits for all three simultaneously.
        allOps.join();

        // Now all three are done — .join() here is instant, no blocking.
        log.info("[Main] CF5 all operations finished:");
        log.info("[Main]  > {}", operation1.join());
        log.info("[Main]  > {}", operation2.join());
        log.info("[Main]  > {}", operation3.join());
    }

    /* What this teaches:
        allOf() = wait for multiple tasks to ALL finish
        exceptionally() = async try-catch that provides fallback values
        Even if some tasks fail, you can recover and still complete */
}
