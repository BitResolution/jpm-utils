package com.bitresolution.jpm.utils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * An ArrayList-backed implementation of a blocking FifoQueue.
 *
 * @param <T> type of items the queue can contain.
 */
public class ArrayListBlockingFifoQueue<T> extends AbstractBlockingFifoQueue<T> {

    public ArrayListBlockingFifoQueue(int capacity) {
        super(capacity, new ArrayList<T>());
    }

    @Override
    protected T pop() {
        return queue.remove(0);
    }
}
