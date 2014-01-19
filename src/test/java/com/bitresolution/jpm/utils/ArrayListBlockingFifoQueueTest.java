package com.bitresolution.jpm.utils;

public class ArrayListBlockingFifoQueueTest extends AbstractBlockingFifoQueueTest {

    protected FifoQueue<String> newFixedCapacityQueue(int capacity) {
        return new ArrayListBlockingFifoQueue<String>(capacity);
    }
}
