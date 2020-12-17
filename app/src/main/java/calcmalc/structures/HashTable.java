package calcmalc.structures;

/**
 * Hash table data structure. This resembles java HashMap data structure.
 * All operations are done in amortized constant time time O(1) or O(m) where m is the number of entries in an index
 * All keys for the Hash table are always strings, keys are not generic types
 * @param <V> the generic value to be contained in the hashtable
 */
public class HashTable<V> {
    /**
     * List of entries in the hashtable, sometimes also known as buckets
     */
    private List<HashTableEntry<V>>[] values;
    /**
     * The amount of entries (buckets) the HashTable contains, the larger the size less likely to be collisions but more memory will be used
     */
    private int size;

    /**
     * Constructor for HashTable data structure
     */
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
     * calculates a hash value for the given key
     * Method is inspired from tirakirja (5.2.1) and jdk source code
     * @param key the key to calculate the hash for
     * @return the integer value for the calculated hash
     */
    private int calculateHash(String key) {
        int hashCode = 0;

        for (char ch : key.toCharArray()) {
            hashCode = 31 * hashCode + ch;
        }

        return hashCode;
    }

    /**
     * Method calculates index for a give hashcode
     * @param hashCode a hash code for any key in the hash table
     * @return an index for the given key
     */
    private int calculateIndex(int hashCode) {
        return hashCode & (size - 1);
    }

    /**
     * Method places or updates a given value in the hash table
     * @param key entry for the value
     * @param value the actual value to store in the hash table
     */
    public void placeOrUpdate(String key, V value) {
        int hashCode = calculateHash(key);
        int index = calculateIndex(hashCode);

        HashTableEntry<V> entry = find(key, hashCode, index);

        if (entry == null) {
            values[index].append(new HashTableEntry<>(key, value, hashCode));
        } else {
            entry.setValue(value);
        }
    }

    /**
     * Helper function to retrive a record from a give index
     * @param key entry to find
     * @param hashCode the hash code value for the key
     * @param index the index location for the entry to find
     * @return Null if not found or the actual entry if found
     */
    private HashTableEntry<V> find(String key, int hashCode, int index) {
        List<HashTableEntry<V>> entries = values[index];

        for (int i = 0; i < entries.size(); ++i) {
            HashTableEntry<V> entry = entries.get(i);
            if (entry.hashCode() == hashCode && entry.getKey().equals(key)) {
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
    public V get(String key) {
        int hashCode = calculateHash(key);
        int index = calculateIndex(hashCode);

        HashTableEntry<V> entry = find(key, hashCode, index);

        if (entry == null) {
            return null;
        }

        return entry.getValue();
    }
}
