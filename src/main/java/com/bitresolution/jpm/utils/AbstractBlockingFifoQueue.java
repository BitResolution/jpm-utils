package com.bitresolution.jpm.utils;

import java.util.List;

/**
 * An base class for implementations of a blocking FifoQueue. Subclasses should provide a list implementation via the
 * constructor and a means of removing the head element of the list by overriding pop().
 *
 * @param <T> type of items the queue can contain.
 */
abstract class AbstractBlockingFifoQueue<T> implements FifoQueue<T> {

    protected final List<T> queue;
    private final int capacity;

    protected AbstractBlockingFifoQueue(int capacity, List<T> backingList) {
        this.capacity = capacity;
        this.queue = backingList;
    }

    /**
     * Adds an element to the tail of the queue. If the queue is currently full (size == capacity) then
     * the method will block until space becomes available. This is a non-interruptable blocking call, however
     * any interrupt status will be propagated to the calling thread.
     *
     * @param item the element to insert
     */
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

    /**
     * Removes the element at the head of the queue. If the queue is currently empty then
     * the method will block until an element becomes available. This is a non-interruptable blocking call, however
     * any interrupt status will be propagated to the calling thread.
     *
     * @return T the oldest element in the queue
     */
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
        T item = pop();
        notifyAll(); //wake up any producers
        return item;
    }

    /**
     * Implementations override this to provide a way to get the head element of the list, e.g. ArrayList
     * does not have poll()
     * @return
     */
    protected abstract T pop();

    /**
     * @return the current size of the queue
     */
    @Override
    public int size() {
        return queue.size();
    }

    /**
     * @return the maximum number of items the queue can hold at any one time
     */
    @Override
    public int getCapacity() {
        return capacity;
    }
}
