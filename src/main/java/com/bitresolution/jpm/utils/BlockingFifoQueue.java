package com.bitresolution.jpm.utils;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingFifoQueue<T> implements FifoQueue<T> {


    private final Queue<T> queue;
    private final int capacity;

    public BlockingFifoQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<T>();
    }

    @Override
    public synchronized void enqueue(T item) {
        queue.add(item);
    }

    @Override
    public synchronized T dequeue() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }

    public int getCapacity() {
        return capacity;
    }
}
