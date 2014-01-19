package com.bitresolution.jpm.utils;

public class LinkedListBlockingFifoQueueTest extends AbstractBlockingFifoQueueTest {

    protected FifoQueue<String> newFixedCapacityQueue(int capacity) {
        return new LinkedListBlockingFifoQueue<String>(capacity);
    }
}
