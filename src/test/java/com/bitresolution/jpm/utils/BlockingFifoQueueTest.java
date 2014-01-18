package com.bitresolution.jpm.utils;

import org.junit.Test;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class BlockingFifoQueueTest {

    @Test
    public void shouldBeAbleToAddElementToUnderCapacityQueue() {
        //given
        BlockingFifoQueue<String> queue = new BlockingFifoQueue<String>(1);
        assumeThat(queue.size(), is(0));
        assumeThat(queue.getCapacity(), is(1));
        assumeThat(queue.size(), is(lessThanOrEqualTo(queue.getCapacity())));
        //when
        queue.enqueue("item");
        //then
        assertThat(queue.size(), is(1));
        assertThat(queue.getCapacity(), is(1));
    }
}
