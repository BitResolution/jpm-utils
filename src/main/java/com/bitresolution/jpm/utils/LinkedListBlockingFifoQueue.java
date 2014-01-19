package com.bitresolution.jpm.utils;

import java.util.LinkedList;

/**
 * An LinkedList-backed implementation of a blocking FifoQueue. This implementation is more efficient
 * than ArraryListBlockingFifoQueue since it doesn't require the constant management of the array underlying
 * ArrayList.
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
