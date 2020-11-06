package calcmalc.structures;

import calcmalc.structures.Listable;

/**
 * Stack data structure.
 * Stack is a LIFO data structure similar to list. Elements can only be pushed onto the stack and poped from the top of the stack.
 * @param <T> the generic type to be contained in the stack
 */
public class Stack<T> {
    private Listable<T> list;

    public Stack(Listable<T> list) {
        this.list = list;
    }

    /**
     * Method push adds element to the top of the stack
     * @param element to be pushed onto the top
     */
    public void push(T element) {
        list.push(element);
    }

    /**
     * Method pops element from the top of the stack, which also removes it from the stack
     * @return the popped or top element
     */
    public T pop() {
        T top = list.getLast();

        if (top == null) {
            return null;
        }

        int head = list.getHead();
        list.remove(head);
        return top;
    }

    /**
     * Method peeks or checks what is the current element on the top of the queue
     * @return T the top element or null if stack is empty
     */
    public T peek() {
        return list.getLast();
    }

    /**
     * Method checks if the stack has any elements on it.
     * @return true if empty, otherwise false
     */
    public boolean isEmpty() {
        return list.getLast() == null;
    }
}