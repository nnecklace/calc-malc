package calcmalc.structures;

/**
 * List data structure. This is the same as javas ArrayList
 * All operations are done in constant time O(1) apart from the grow function which is only called when the size of the list is a power of 2
 * @param <T> the generic type to be contained in the list
 */
public class List<T> {
    private T[] list;
    private int head;
    private int size;

    public List(int capacity) {
        this.size = capacity + 8; 
        head = capacity;
        list = (T[]) new Object[this.size]; // should not be done this way, but Java doesn't support generic arrays
    }

    /**
     * Constructor for list data structure
     */
    public List() {
        this(0);
    }

    /**
     * Method retrives an element from the list at the desired index
     * 
     * @param index the position of the element to be retrieved
     * @return T the generic element at the given position of the list or null if
     *         index is negitive or above the current size of the list
     */
    public T get(int index) {
        if (index >= head || index < 0) {
            throw new IllegalArgumentException("Index out of range");
        }

        return list[index];
    }

    /**
     * Method for setting values at any index in the list
     * @param index to be placed
     * @param value to be inserted
     */
    public void set(int index, T value) {
        if (index >= 0 && index <= head) {
            if (index == head) {
                if (index == size) {
                    grow();
                }
                head++; 
            }

            list[index] = value;
        } else {
            throw new IllegalArgumentException("Index out of range");
        }
    }
    
    /**
     * Method getter for size property
     * @return size property
     */
    public int getSpace() {
        return size;
    }

    /**
     * Method getter for head property
     * @return head property
     */
    public int size() {
        return head;
    }

    /**
     * TODO: Check if method is needed
     * Method removes element at the given index and creates new array 
     * @param index index of the element to be removed
     */
    public T remove(int index) {
        if (index < head && index >= 0) {
            T el = list[index];
            int shiftSize = head - index - 1;
            if (shiftSize > 0) {
                System.arraycopy(list, index + 1, list, index, shiftSize);
            }
            list[--head] = null;

            return el;
        } else {
            throw new IllegalArgumentException("Index out of range");
        }
    }

    /**
     * Method pushes element to the end of the list. If list is full list remains unchanged.
     * If head has reached the end of the list, the list size will be increased.
     * @param element The element to push to the end of the list
     */
    public void append(T element) {
        if ((head + 1) == size) {
            grow();
        }

        list[head++] = element;
    }

    /**
     * Method increases the size of the list by doubling it until it is larger than the new requested size.
     * Method creates a new list and copies the elements from the old list to the new list, and discards the old list.
     */
    private void grow() {
        // at some point size will overflow, but usually java runs out of memory way before that
        size *= 2;

        T[] copy = (T[]) new Object[size];

        for (int i = 0; i < size / 2; ++i) {
            copy[i] = list[i]; 
        }

        list = copy;
    }

    /**
     * Boolean function to check wheather lis is empty or not
     * @return true if there are no elements in the list
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
