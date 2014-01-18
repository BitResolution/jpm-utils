package com.bitresolution.jpm.utils;

public class BlockingFifoQueue<T> implements FifoQueue<T> {

    @Override
    public synchronized void enqueue(T o) {
    }

    @Override
    public synchronized T dequeue() {
        return null;
    }
}
