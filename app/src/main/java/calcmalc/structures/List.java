package calcmalc.structures;

/**
 * List data structure. This is the same as javas ArrayList
 * All operations are done in constant time O(1) apart from the grow function, which is only called when the head of the list has reached the end of the the array
 * @param <T> the generic type to be contained in the list
 * @author nnecklace
 */
public class List<T> {
    /**
     * The underlying array value the list is stored in
     */
    private T[] list;
    /**
     * head property indicates where in the list we currently are pointing at
     * Also tells how big the list currently is since head will always point to the end of the list
     */
    private int head;
    /**
     * The size of the underlying array value, not the "size" of the List. 
     * The underlying array size should be larger than the head property
     */
    private int size;

    /**
     * Construtor for list with given size
     * @param initialSize indicates the the size of the list
     */
    public List(int initialSize) {
        this.size = initialSize + 8; 
        head = initialSize;
        list = (T[]) new Object[this.size]; // should not be done this way, but Java doesn't support generic arrays
    }

    /**
     * Constructor for list data structure with a starting capacity of 0
     * Which means that the initial size will be 0
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
     * @throws IllegalArgumentException if index is not within the legal ranges of the list
     */
    public T get(int index) {
        if (index >= head || index < 0) {
            throw new IllegalArgumentException("Index out of range");
        }

        return list[index];
    }

    /**
     * Method for setting values at any index in the list
     * Increases the size of the underlying array if index happens to be the last legal position
     * And the last legal position is also the same as the size of the underlying array
     * Set allows elements to be set 1 position over the current size, in this case list will call append since it is the same operation
     * @param index to be placed
     * @param value to be inserted
     * @throws IllegalArgumentException if index is not within the legal ranges of the list
     */
    public void set(int index, T value) {
        if (index >= 0 && index <= head) {
            if (index == head) {
                append(value);
            } else {
                list[index] = value;
            }
        } else {
            throw new IllegalArgumentException("Index out of range");
        }
    }
    
    /**
     * Method getter for size property
     * Returns the actual size of the current array the list is built on
     * @return size property
     */
    public int getSpace() {
        return size;
    }

    /**
     * Method getter for head property the head indicates what is the current size of the list
     * @return head property
     */
    public int size() {
        return head;
    }

    /**
     * Method pushes element to the end of the list. 
     * If head has reached the end of the list, the array size of the list will be increased.
     * @param element The element to push to the end of the list
     */
    public void append(T element) {
        if ((head + 1) == size) {
            grow();
        }

        list[head++] = element;
    }

    /**
     * Method increases the size of the array by doubling it until it is larger than the new requested size.
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
