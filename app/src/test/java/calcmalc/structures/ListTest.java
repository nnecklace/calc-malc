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
    public void testRemoveElementAtIncorrectIndex() {
        Listable<Integer> list = new List<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.remove(-1);
        });
    }

    @Test
    public void testRemoveElementAtIncorrectIndex2() {
        Listable<Integer> list = new List<>();
        list.push(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.remove(1);
        });
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
    public void testSetInsertsValueAtIndex() {
        Listable<Integer> list = new List<>();
        list.set(0, 2);
        assertEquals((Integer)2, list.get(0));
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
    public void testListShouldBeEmptyWhenRemoingInReverse() {
        Listable<Integer> list = new List<>();
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        assertEquals(4, list.size());
        list.remove(0);
        assertEquals(3, list.size());
        list.remove(1);
        assertEquals(2, list.size());
        list.remove(1);
        assertEquals(1, list.size());
        list.remove(0);
        assertEquals(0, list.size());
    }

    @Test
    public void testListSetToPowerOfTwo() {
        Listable<Integer> list = new List<>();
        list.push(1); // head = 0
        list.push(1); // head = 1
        list.push(1); // hjead = 2
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.set(8,2);
        assertEquals(9, list.size());
        assertEquals((Integer)2, list.get(8));
        assertEquals(16, list.getSpace());
    }

    @Test
    public void testEnsureSizeStaysSameEvenAfterRemoving() {
        Listable<Integer> list = new List<>();
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);
        assertEquals(16, list.getSpace());
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        list.remove(0);
        assertEquals(16, list.getSpace());
    }

    @Test
    public void testSetThrowsOnIllegalIndex() {
        Listable<Integer> list = new List<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.set(-1, 2);
        });
    }

    @Test
    public void testSetThrowsOnIllegalIndex2() {
        Listable<Integer> list = new List<>();

        list.push(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            list.set(2, 2);
        });
    }
}
