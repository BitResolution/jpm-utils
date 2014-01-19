package com.bitresolution.jpm.utils;

class Consumer implements Runnable {

    private final FifoQueue<String> queue;
    private String item;
    private boolean complete;
    private boolean running;

    Consumer(FifoQueue<String> queue) {
        this.queue = queue;
        this.complete = false;
        this.running = false;
    }

    @Override
    public void run() {
        this.running = true;
        this.item = queue.dequeue();
        this.complete = true;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isRunning() {
        return running;
    }

    public String getItem() {
        return item;
    }
}
