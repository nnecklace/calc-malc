package calcmalc.structures;

import java.util.Random;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListTest {
    @Test
    public void testListIsCreatedWithCorrectSize() {
        Listable<Integer> list = new List<>();
        assertEquals(list.getSpace(), 8);
    }
    
    @Test
    public void testElementCanBePushedToList() {
        Listable<Integer> list = new List<>();
        list.push(1);
        assertEquals(list.get(0), (Integer)1);
    }

    @Test
    public void testElementsCanBePushedToList() {
        Listable<Integer> list = new List<>();
        list.push(1);
        list.push(2);
        list.push(3);
        list.push(4);
        assertEquals(list.get(0), (Integer)1);
        assertEquals(list.get(1), (Integer)2);
        assertEquals(list.get(2), (Integer)3);
        assertEquals(list.get(3), (Integer)4);
    }

    @Test
    public void testListSizeGetExponentiallyLarger() {
        Listable<Integer> list = new List<>();
        for (int i = 0; i <= 8; ++i) {
            list.push(i);
        }
        assertEquals(list.getSpace(), 16);

        for (int i = 8; i < 16; ++i) {
            list.push(i);
        }

        assertEquals(list.getSpace(), 32);

        for (int i = 16; i < 32; ++i) {
            list.push(i);
        }

        assertEquals(list.getSpace(), 64);
    }

    @Test
    public void testGetLastAlwaysGetsLatestEntry() {
        Listable<Integer> list = new List<>();
        Random r = new Random(1337);
        for (int i = 0; i < 1e6; ++i) {
            list.push(r.nextInt(500));
        }

        Integer last = -1;

        list.push(last);

        assertEquals(list.getLast(), last);

        for (int i = 0; i < 1e4; ++i) {
            list.push(r.nextInt(100));
        }

        last = -100;

        list.push(last);

        assertEquals(list.getLast(), last);
    }

    @Test
    public void testRemoveElementsFromList() {
        Listable<Integer> list = new List<>();
        Random r = new Random(1337);
        for (int i = 0; i < 1e6; ++i) {
            list.push(r.nextInt(500));
        }
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i = 1; i <= 100; ++i) {
            indexes.add(r.nextInt(1000000));
        }

        for (Integer i : indexes) {
            assertNotNull(list.get(i));
        }

        for (Integer i : indexes) {
            list.remove(i);
        }

        for (Integer i : indexes) {
            assertNull(list.get(i));
        }
    }

    @Test
    public void testRemoveElementAtIncorrectIndex() {
        Listable<Integer> list = new List<>();
        assertEquals(list.size(), 0);
        list.remove(-1);
        assertEquals(list.size(), 0);
        list.remove(8);
        assertEquals(list.size(), 0);
    }

    @Test
    public void testGetElementAtIncorrectIndex() {
        Listable<Integer> list = new List<>();
        Integer i = list.get(-1);
        assertNull(i);
        list.push(1);
        list.push(2);
        i = list.get(2);
        assertNull(i);
        i = list.get(3);
        assertNull(i);
        assertEquals((Integer)1, list.get(0));
        assertEquals((Integer)2, list.get(1));
        i = list.get(8);
        assertNull(i);
    }

    @Test
    public void testGetLastOnEmptyList() {
        Listable<Integer> list = new List<>();
        assertNull(list.getLast());
    }

    @Test
    public void testSetInsertsValueAtIndex() {
        Listable<Integer> list = new List<>();
        list.set(2, 2);
        assertEquals((Integer)2, list.get(2));
    }

    @Test
    public void testSetReplacesValueAtIndex() {
        Listable<Integer> list = new List<>();
        list.push(1);
        list.push(5);
        list.push(7);
        assertEquals((Integer)7, list.get(2));
        list.set(2, 2);
        assertEquals((Integer)2, list.get(2));
    }

    @Test
    public void testSetDeterminesNewSizeOfList() {
        Listable<Integer> list = new List<>();
        list.set(0, 1);
        assertEquals(1, list.size());
        list.set(5, 5);
        assertEquals(6, list.size());
    }

    @Test
    public void testSetGrowsTheListSize() {
        Listable<Integer> list = new List<>();
        list.set(10, 1);
        assertEquals(16, list.getSpace());
    }

    @Test
    public void testSetThrowsOnIllegalIndex() {
        Listable<Integer> list = new List<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.set(-1, 2);
        });
    }
}
