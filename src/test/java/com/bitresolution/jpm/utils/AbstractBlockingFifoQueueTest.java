package com.bitresolution.jpm.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public abstract class AbstractBlockingFifoQueueTest {

    private static final String ITEM = "item";
    private static final String ITEM_A = "item A";
    private static final String ITEM_B = "item B";

    @Test
    public void shouldBeAbleToAddElementToUnderCapacityQueue() {
        //given
        FifoQueue<String> queue = newFixedCapacityQueue(1);
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
        FifoQueue<String> queue = newFixedCapacityQueue(2);
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

        FifoQueue<String> queue = newFixedCapacityQueue(1);
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

    @Test
    public void shouldBlockUntilSpaceIsAvailableWhenAddingToQueue() {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(2);

        FifoQueue<String> queue = newFixedCapacityQueue(1);
        queue.enqueue(ITEM_A);
        Producer producer = new Producer(queue, ITEM_B);
        Consumer consumer = new Consumer(queue);
        assumeThat(consumer.isComplete(), is(false));

        //when
        executor.execute(producer);
        await().untilCall(to(producer).isRunning(), is(true));
        assertThat(consumer.isComplete(), is(false));

        executor.execute(consumer);
        await().untilCall(to(consumer).isRunning(), is(true));

        //then
        await().untilCall(to(producer).isComplete(), is(true));
        await().untilCall(to(consumer).isComplete(), is(true));
        assertThat(consumer.getItem(), is(ITEM_A));
        assertThat(queue.size(), is(1));
    }

    //TODO introduce a countdown latch to the Producer/Consumer classes to make
    //this test cleaner (less loops). Would also simplify the earlier tests where the
    //await().untilCall(to(producer).isRunning(), is(true)) construct is used for
    //signalling
    @Test
    public void shouldAllowConcurrentAccess() {
        //given
        int actorCount = 20;
        int concurrentActors = 10;

        ExecutorService executor = Executors.newFixedThreadPool(concurrentActors);

        FifoQueue<String> queue = newFixedCapacityQueue(2);
        List<Producer> producers = new ArrayList<Producer>();
        for(int i = 0; i < actorCount; i++) {
            producers.add(new Producer(queue, ITEM + "_" + i));
        }
        List<Consumer> consumers = new ArrayList<Consumer>();
        for(int i = 0; i < actorCount; i++) {
            consumers.add(new Consumer(queue));
        }

        //when
        for(int i = 0; i < actorCount; i++) {
            executor.execute(producers.get(i));
            executor.execute(consumers.get(i));
        }

        //then
        for(int i = 0; i < actorCount; i++) {
            await().untilCall(to(producers.get(i)).isComplete(), is(true));
        }
        for(int i = 0; i < actorCount; i++) {
            await().untilCall(to(consumers.get(i)).isComplete(), is(true));
        }
        assertThat(queue.size(), is(0));
    }

    protected abstract FifoQueue<String> newFixedCapacityQueue(int capacity);
}
