package calcmalc.structures;

import java.util.Random;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import calcmalc.structures.List;
import calcmalc.structures.Listable;

public class ListTest {
    @Test
    public void testListIsCreatedWithCorrectSize() {
        Listable<Integer> list = new List<>();
        assertEquals(list.getSize(), 8);
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
        assertEquals(list.getSize(), 16);

        for (int i = 8; i < 16; ++i) {
            list.push(i);
        }

        assertEquals(list.getSize(), 32);

        for (int i = 16; i < 32; ++i) {
            list.push(i);
        }

        assertEquals(list.getSize(), 64);
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
    public void testIsFull() {
        Listable<Integer> list = new List<>();
        for (int i = 0; i < 10000000; i++) {
            list.push(i);
        }

        assertTrue(list.isFull());

        Integer last = list.getLast();

        list.push(-1);
        list.push(-2);
        list.push(-3);

        assertEquals(last, list.getLast());
    }
}
