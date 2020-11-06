package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;
import calcmalc.structures.Queue;
import calcmalc.structures.List;

public class QueueTest {
    @Test
    public void testQueueHeadMovesWhileTailStays() {
        Queue<Integer> queue = new Queue<>(new List<Integer>());
        assertEquals(0, queue.getHead());
        assertEquals(0, queue.getTail());
        queue.enqueue(1);
        assertEquals(1, queue.getHead());
        assertEquals(0, queue.getTail());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue<Integer> queue = new Queue<>(new List<Integer>());
        assertNull(queue.dequeue());
        queue.enqueue(1);
        assertEquals((Integer)1, queue.dequeue());
    }
    
    @Test
    public void testEnqueueAndDequeueMultiple() {
        Queue<Integer> queue = new Queue<>(new List<Integer>());
        assertNull(queue.dequeue());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals((Integer)1, queue.dequeue());
        assertEquals((Integer)2, queue.dequeue());
        assertEquals((Integer)3, queue.dequeue());
    }
}
