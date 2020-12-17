package calcmalc.structures;

/**
 * Stack data structure.
 * Stack is a LIFO data structure similar to list. Elements can only be pushed onto the stack and poped from the top of the stack.
 * All operations are done in constant O(1) time.
 * @param <T> the generic type to be contained in the stack
 * @author nnecklace
 */
public class Stack<T> {
    /**
     * The list that the stack is built from
     */
    private List<T> list;
    /**
     * Pointer which points to the current top of the stack
     */
    private int top;

    /**
     * Constructor for stack data structure
     */
    public Stack() {
        this.list = new List<>();
        this.top = 0;
    }

    /**
     * Method push adds element to the current top of the stack
     * @param element to be pushed onto the top
     */
    public void push(T element) {
        list.set(top++, element);
    }

    /**
     * Method pops element from the top of the stack, which also removes it from the stack
     * Returns null if stack is empty
     * @return the popped topped element or null if stack is empty
     */
    public T pop() {
        if (top == 0) {
            return null;
        }

        return list.get(--top);
    }

    /**
     * Method peeks or checks what is the current element on the top of the stack
     * If stack is empty the method returns null
     * @return T the top element or null if stack is empty
     */
    public T peek() {
        if (top == 0) {
            return null;
        }

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
     * Method returns top pointer of the stack, which also indicates what the size of the stack is
     * @return the current top index of the stack which also tells the size of the stack
     */
    public int size() {
        return top;
    }
}