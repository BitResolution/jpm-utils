package com.bitresolution.jpm.utils;

class Producer implements Runnable {

    private final BlockingFifoQueue<String> queue;
    private final String item;
    private boolean complete;
    private boolean running;

    Producer(BlockingFifoQueue<String> queue, String item) {
        this.queue = queue;
        this.item = item;
        this.complete = false;
        this.running = false;
    }

    @Override
    public void run() {
        this.running = true;
        queue.enqueue(item);
        this.complete = true;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isRunning() {
        return running;
    }
}
