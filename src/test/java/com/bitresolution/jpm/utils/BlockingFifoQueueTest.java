package com.bitresolution.jpm.utils;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static com.jayway.awaitility.Duration.FIVE_SECONDS;
import static com.jayway.awaitility.Duration.ONE_SECOND;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class BlockingFifoQueueTest {

    private static final String ITEM = "item";
    private static final String ITEM_A = "item A";
    private static final String ITEM_B = "item B";

    @Test
    public void shouldBeAbleToAddElementToUnderCapacityQueue() {
        //given
        BlockingFifoQueue<String> queue = new BlockingFifoQueue<String>(1);
        assumeThat(queue.size(), is(0));
        assumeThat(queue.getCapacity(), is(1));
        assumeThat(queue.size(), is(lessThanOrEqualTo(queue.getCapacity())));
        //when
        queue.enqueue(ITEM);
        //then
        assertThat(queue.size(), is(1));
        assertThat(queue.getCapacity(), is(1));
    }

    @Test
    public void shouldBeAbleToRemoveOldestElementFromNonEmptyQueue() {
        //given
        BlockingFifoQueue<String> queue = new BlockingFifoQueue<String>(2);
        queue.enqueue(ITEM_A);
        queue.enqueue(ITEM_B);
        assumeThat(queue.size(), is(2));
        //when
        String item = queue.dequeue();
        //then
        assertThat(item, is(ITEM_A));
        assertThat(queue.size(), is(1));
    }

    @Test
    public void shouldBlockUntilElementIsAvailableWhenRemovingFromQueue() {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(2);

        BlockingFifoQueue<String> queue = new BlockingFifoQueue<String>(1);
        Producer producer = new Producer(queue, ITEM);
        Consumer consumer = new Consumer(queue);
        assumeThat(consumer.isComplete(), is(false));

        //when
        executor.execute(consumer);
        //out of order execution means unless we block until consumer.isRunning we might check the state too early
        //if the queue is not actually blocking on dequeues!
        await().untilCall(to(consumer).isRunning(), is(true));
        assertThat(consumer.isComplete(), is(false));

        executor.execute(producer);
        await().untilCall(to(producer).isRunning(), is(true));

        //then
        await().untilCall(to(producer).isComplete(), is(true));
        await().untilCall(to(consumer).isComplete(), is(true));
        assertThat(consumer.getItem(), is(ITEM));
    }


    private static class Producer implements Runnable {

        private final BlockingFifoQueue<String> queue;
        private final String item;
        private boolean complete;
        private boolean running;

        private Producer(BlockingFifoQueue<String> queue, String item) {
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

    private static class Consumer implements Runnable {

        private final BlockingFifoQueue<String> queue;
        private String item;
        private boolean complete;
        private boolean running;

        private Consumer(BlockingFifoQueue<String> queue) {
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
}
