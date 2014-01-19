package com.bitresolution.jpm.utils;

import java.util.LinkedList;

/**
 * An ArrayList-backed implementation of a blocking FifoQueue.
 *
 * @param <T> type of items the queue can contain.
 */
public class LinkedListBlockingFifoQueue<T> extends AbstractBlockingFifoQueue<T> {

    public LinkedListBlockingFifoQueue(int capacity) {
        super(capacity, new LinkedList<T>());
    }

    @Override
    protected T pop() {
        return ((LinkedList<T>) queue).poll();
    }
}
