package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    public void testQueueIsEmpty() {
        Queue<Integer> queue = new Queue<>(new List<Integer>());
        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testQueueSize() {
        Queue<Integer> queue = new Queue<>(new List<Integer>());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);// h = 3
        assertEquals(3, queue.size());
        queue.dequeue(); // h =3 t =1
        assertEquals(2, queue.size());
        queue.enqueue(3);
        queue.enqueue(3);
        assertEquals(4, queue.size());
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        assertEquals(0, queue.size());
    }

    @Test
    public void testQueueSizeCannotBeNegative() {
        Queue<Integer> queue = new Queue<>(new List<Integer>());
        queue.dequeue();
        queue.dequeue();
        assertEquals(0, queue.size());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.size());
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        assertEquals(0, queue.size());
    }
}
