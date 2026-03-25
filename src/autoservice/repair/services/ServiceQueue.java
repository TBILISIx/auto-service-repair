package autoservice.repair.services;

import java.util.LinkedList;
import java.util.Queue;

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
        System.out.println("Queue contents: " + queue);
    }

}