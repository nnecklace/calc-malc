package calcmalc.structures;

/**
 * Stack data structure.
 * Stack is a LIFO data structure similar to list. Elements can only be pushed onto the stack and poped from the top of the stack.
 * @param <T> the generic type to be contained in the stack
 */
public class Stack<T> {
    private Listable<T> list;
    private int top;

    /**
     * Constructor for stack data structure
     * @param list the underlying list which stack is built upon. List may contain values already
     */
    public Stack(Listable<T> list) {
        this.list = list;
        this.top = 0;
    }

    /**
     * Method push adds element to the top of the stack
     * @param element to be pushed onto the top
     */
    public void push(T element) {
        list.set(top++, element);
    }

    /**
     * Method pops element from the top of the stack, which also removes it from the stack
     * @return the popped or top element
     */
    public T pop() {
        if (top == 0) {
            return null;
        }

        return list.get(--top);
    }

    /**
     * Method peeks or checks what is the current element on the top of the queue
     * @return T the top element or null if stack is empty
     */
    public T peek() {
        return list.get(top - 1);
    }

    /**
     * Method checks if the stack has any elements on it.
     * @return true if empty, otherwise false
     */
    public boolean isEmpty() {
        return top == 0;
    }

    /**
     * Method returns top pointer of the stack
     * @return the current top index of the stack
     */
    public int size() {
        return top;
    }
}