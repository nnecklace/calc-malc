package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;

public class StackTest {
    @Test
    public void testStackCanBePushed() {
        Stack<Integer> stack = new Stack<>();

        Integer a = 5;

        stack.push(a);

        assertEquals(stack.peek(), a);
    }
    
    @Test
    public void testStackCanBePeeked() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);

        Integer top = stack.peek();

        assertEquals(top, (Integer) 10);
    }

    @Test
    public void testStackCanBePushedMultipleTimes() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.push(6);

        assertEquals(stack.peek(), (Integer) 6);
    }

    @Test
    public void testStackCanBePopped() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(stack.peek(), (Integer) 3);
        stack.pop();
        assertEquals(stack.peek(), (Integer) 2);
        stack.pop();
        assertEquals(stack.peek(), (Integer) 1);
        stack.pop();
        assertEquals(stack.peek(), null);
    }

    @Test
    public void testStackSizeIsDynamic() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
        stack.pop();
        stack.pop();
        assertEquals(1, stack.size());
        stack.push(5);
        assertEquals(2, stack.size());
    }

    @Test
    public void testStackPoppedEmpty() {
        Stack<Integer> stack = new Stack<>();
        assertNull(stack.pop());
    }

    @Test
    public void testStackCanBePeekedWhenEmpty() {
        Stack<Integer> stack = new Stack<>();
        assertNull(stack.peek());
        stack.push(0);
        assertEquals((Integer)0, stack.peek());
    }
}
