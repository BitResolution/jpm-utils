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
        boolean interrupted = false;
        try {
            while(queue.size() >= capacity) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                    //fall through, retry and propagate interrupt status later
                }
            }
        }
        finally {
            if(interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        queue.add(item);
        notifyAll(); //wake up any consumers
    }

    @Override
    public synchronized T dequeue() {
        boolean interrupted = false;
        try {
            while(queue.size() == 0) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                    //fall through, retry and propagate interrupt status later
                }
            }
        }
        finally {
            if(interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        T item = queue.poll();
        notifyAll(); //wake up any producers
        return item;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}
