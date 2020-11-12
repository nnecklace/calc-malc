package calcmalc.structures;

public interface Listable<T> {
    public T get(int index);
    public int size();
    public int getSpace();
    public T getLast();
    public void push(T element);
    public void remove(int index);
    public boolean isFull();
    public boolean isEmpty();
}