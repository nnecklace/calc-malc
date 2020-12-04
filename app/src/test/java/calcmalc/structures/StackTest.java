package calcmalc.structures;

import org.junit.Test;
import static org.junit.Assert.*;

public class StackTest {
    @Test
    public void testStackCanBePushed() {
        Stack<Integer> stack = new Stack<>(new List<Integer>());

        Integer a = 5;

        stack.push(a);

        assertEquals(stack.peek(), a);
    }
    
    @Test
    public void testStackCanBePeeked() {
        Stack<Integer> stack = new Stack<>(new List<Integer>());
        stack.push(10);

        Integer top = stack.peek();

        assertEquals(top, (Integer) 10);
    }

    @Test
    public void testStackCanBePushedMultipleTimes() {
        Stack<Integer> stack = new Stack<>(new List<Integer>());
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
        Stack<Integer> stack = new Stack<>(new List<Integer>());
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
        Stack<Integer> stack = new Stack<>(new List<Integer>());
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
        Stack<Integer> stack = new Stack<>(new List<Integer>());
        assertNull(stack.pop());
    }

    @Test
    public void testStackIsGivenNonEmptyList() {
        List<Integer> l = new List<>();
        l.push(1);
        l.push(2);
        l.push(3);
        Stack<Integer> stack = new Stack<>(l);
        assertEquals((Integer) 3, stack.pop());
    }

    @Test
    public void testStackIsGivenNonEmptyList2() {
        List<Integer> l = new List<>();
        l.push(1);
        l.push(2);
        l.push(3);
        l.push(4);
        l.push(5);
        l.push(6);
        l.push(7);
        l.push(8);
        Stack<Integer> stack = new Stack<>(l);
        assertEquals(8, stack.size());
        assertEquals((Integer) 8, stack.peek());
    }

}
