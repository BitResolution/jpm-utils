package com.bitresolution.jpm.utils;

import org.junit.Test;

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
}
