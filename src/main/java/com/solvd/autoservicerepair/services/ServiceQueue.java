package com.solvd.autoservicerepair.services;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class ServiceQueue<T> {

    private final Queue<T> queue;
    private final Integer capacity;

    public ServiceQueue(Integer capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public boolean add(T item) {
        if (queue.size() < capacity) {
            queue.add(item);
            return true;
        } else {
            return false;
        }
    }

    public T poll() {
        return queue.poll();
    }

    public T peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void displayQueue() {
        log.info("Queue contents: " + queue);
    }

}