package com.bitresolution.jpm.utils;

public interface FifoQueue<T> {

    public void enqueue(T obj);
    public T dequeue();
}
