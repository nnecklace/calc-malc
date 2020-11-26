package calcmalc.structures;

/**
 * Hash table data structure. This resembles java HashMap data structure.
 * All operations are done in amortized constant time time O(1) or O(m) where m is the number of entries in an index
 * @param <K, V> the generic types to be contained in the hashtable
 */
public class HashTable<K, V> {
    private List<HashTableEntry<K, V>>[] values;
    private int size;

    public HashTable() {
        // size should be some power of 2, so the index can be calculated easier
        // size - 1 in binary will be all zeros up until the bit which size represents
        // e.g. 16 - 1 = 15 <=> 0000 1111
        // e.g. 32 - 1 = 31 <=> 0001 1111
        // this way calculate index will never be larger than size
        this.size = 16;
        this.values = new List[size];
        fill();
    }

    /**
     * Function fills the hash table indexes to avoid any null pointers
     */
    private void fill() {
        for (int i = 0; i < size; ++i) {
            values[i] = new List<>();
        }
    }

    /**
     * Method calculates index for a give key
     * @param key to calculate
     * @return an index for the given key
     */
    private int calculateIndex(K key) {
        return key.hashCode() & (size - 1);
    }

    /**
     * Method places or updates a given value in the hash table
     * @param key entry for the value
     * @param value the actual value to store in the hash table
     */
    public void placeOrUpdate(K key, V value) {
        int index = calculateIndex(key);

        HashTableEntry<K, V> entry = find(key);

        if (entry == null) {
            values[index].push(new HashTableEntry<>(key, value, key.hashCode()));
        } else {
            entry.setValue(value);
        }
    }

    /**
     * Helper function to retrive a record from a give index
     * @param key entry to find
     * @return Null if not found or the actual entry
     */
    private HashTableEntry<K, V> find(K key) {
        int index = calculateIndex(key);

        List<HashTableEntry<K, V>> entries = values[index];

        for (int i = 0; i < entries.size(); ++i) {
            HashTableEntry<K, V> entry = entries.get(i);
            // Nullpointer possible ?
            if (entry.hashCode() == key.hashCode() && entry.getKey().equals(key)) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Retrives an entry from the hash table
     * @param key entry to retrieve
     * @return the entry value or null if not found
     */
    public V get(K key) {
        HashTableEntry<K, V> entry = find(key);

        if (entry == null) {
            return null;
        }

        return entry.getValue();
    }
}
