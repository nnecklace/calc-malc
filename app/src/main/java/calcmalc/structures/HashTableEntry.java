package calcmalc.structures;

/**
 * Generic container class for HashTable
 * @param <K> key of the pair to store in the HashTable
 * @param <V> value of the pair to store in the HashTable
 */
public class HashTableEntry<K, V> {
    private V value;
    private K key;
    private int hash;

    /**
     * Constructor for a HashTableEntry element 
     * @param key the key associated with the entry
     * @param value  the value associated with the entry
     * @param hash the hashcode for the entry
     */
    public HashTableEntry(K key, V value, int hash) {
        this.key = key;
        this.value = value;
        this.hash = hash;
    }

    /**
     * Getter for key property
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * Getter for value property
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * Setter for value property
     * @param value to be set
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Hashcode function is actually just a getter for hash property
     */
    @Override
    public int hashCode() {
        return hash;
    }
}
