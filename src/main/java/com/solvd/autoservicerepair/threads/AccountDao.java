package com.solvd.autoservicerepair.threads;

import com.solvd.autoservicerepair.utils.SleepUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Faked "Connection" class acting as AccountDao.
 * Simulates DB operations with fake data.
 */

@Getter

@Slf4j

// Dao = data access object
public class AccountDao {

    private final int connectionId;

    public AccountDao(int connectionId) {
        this.connectionId = connectionId;
    }

    public void create(String accountName) {
        log.info("[Connection-{}] CREATE account: '{}'", connectionId, accountName);
    }

    public String get(int id) {
        String fakeData = "Account{id=" + id + ", name='FakeUser-" + id + "', balance=1000.00}";
        log.info("[Connection-{}] GET account id={} => {}", connectionId, id, fakeData);
        return fakeData;
    }

    // Async version of get() - runs the DataBase lookup on the provided executor
    // and returns a CompletableFuture<String> so the caller never blocks.
    // The Executor argument lets the caller control which thread pool does the work.
    public CompletableFuture<String> getAsync(int id, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            SleepUtils.sleep(3000);
            return get(id);
        }, executor);
    }

    public void update(int id, String newName) {
        log.info("[Connection-{}] UPDATE account id={} => new name: '{}'", connectionId, id, newName);
    }

    public void delete(int id) {
        log.info("[Connection-{}] DELETE account id={}", connectionId, id);
    }

    @Override
    public String toString() {
        return "AccountDao{connectionId=" + connectionId + "}";
    }

}
