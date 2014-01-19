package com.bitresolution.jpm.utils;

/**
 * A simple FIFO (First-In First-Out queue) definition.
 *
 * @param <T> type of items the queue can contain.
 */
public interface FifoQueue<T> {

    /**
     * Adds an element to the tail of the queue. If the queue is currently full implementations may either block
     * or return without adding the element, see the specific javadoc for the implementing class.
     *
     * @param obj the item to insert
     */
    public void enqueue(T obj);

    /**
     * Removes the element at the head of the queue. If the queue is currently empty then
     * implementations may either block or return without adding the element, see the specific javadoc
     * for the implementing class.
     *
     * @return T the oldest element in the queue
     */
    public T dequeue();

    /**
     * @return the current size of the queue
     */
    int size();

    /**
     * @return the maximum number of items the queue can hold at any one time
     */
    int getCapacity();
}
