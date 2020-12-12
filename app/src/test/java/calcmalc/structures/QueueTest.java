package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    @Test
    public void testEnqueueAndDequeue() {
        Queue<Integer> queue = new Queue<>();
        assertNull(queue.dequeue());
        queue.enqueue(1);
        assertEquals((Integer) 1, queue.dequeue());
    }

    @Test
    public void testBasicUsageOfQueue() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1); 
        assertEquals(1, queue.size());
        assertEquals((Integer)1, queue.dequeue());
    }
    
    @Test
    public void testEnqueueAndDequeueMultiple() {
        Queue<Integer> queue = new Queue<>();
        assertNull(queue.dequeue());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals((Integer) 1, queue.dequeue());
        assertEquals((Integer) 2, queue.dequeue());
        assertEquals((Integer) 3, queue.dequeue());
    }

    @Test
    public void testQueueIsEmpty() {
        Queue<Integer> queue = new Queue<>();
        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeekFirst() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        assertEquals((Integer)1, queue.peekFirst());
        queue.enqueue(2);
        assertEquals((Integer)1, queue.peekFirst());
        queue.enqueue(7);
        queue.enqueue(12);
        queue.enqueue(100);
        assertEquals((Integer)1, queue.peekFirst());
        queue.dequeue();
        assertEquals((Integer)2, queue.peekFirst());
        queue.dequeue();
        assertEquals((Integer)7, queue.peekFirst());
        queue.dequeue();
        assertEquals((Integer)12, queue.peekFirst());
        queue.dequeue();
        assertEquals((Integer)100, queue.peekFirst());
        queue.dequeue();
        assertNull(queue.peekFirst());
    }

    @Test
    public void testPeekLast() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        assertEquals((Integer)1, queue.peekLast());
        queue.enqueue(2);
        assertEquals((Integer)2, queue.peekLast());
        queue.enqueue(7);
        queue.enqueue(12);
        queue.enqueue(100);
        assertEquals((Integer)100, queue.peekLast());
        queue.dequeue();
        assertEquals((Integer)100, queue.peekLast());
        queue.dequeue();
        assertEquals((Integer)100, queue.peekLast());
        queue.dequeue();
        assertEquals((Integer)100, queue.peekLast());
        queue.dequeue();
        assertEquals((Integer)100, queue.peekLast());
        queue.dequeue();
        assertNull(queue.peekLast());
    }

    @Test
    public void testPeekFirstAndLastReturnNullOnEmptyQueue() {
        Queue<Integer> queue = new Queue<>();
        assertNull(queue.peekFirst());
        assertNull(queue.peekLast());
        queue.enqueue(1);
        assertEquals((Integer)1, queue.peekFirst());
        assertEquals((Integer)1, queue.peekLast());
        queue.enqueue(2);
        assertEquals((Integer)1, queue.peekFirst());
        assertEquals((Integer)2, queue.peekLast());
        queue.dequeue();
        queue.dequeue();
        assertNull(queue.peekFirst());
        assertNull(queue.peekLast());
    }

    @Test
    public void testQueueSize() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3); // h = 3
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
        Queue<Integer> queue = new Queue<>();
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
