import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * JUnit tests for AVL Tree
 */
public class AVLTest {

    private static final int TIMEOUT = 200;
    private AVL<Integer> tree;

    @Before
    public void setup() {
        tree = new AVL<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, tree.size());
        assertNull(tree.getRoot());
    }

    @Test(timeout = TIMEOUT)
    public void testConstructor() {
        /*
              1
             / \
            0   2
        */

        List<Integer> toAdd = new ArrayList<>();
        toAdd.add(1);
        toAdd.add(0);
        toAdd.add(2);
        tree = new AVL<>(toAdd);

        assertEquals(3, tree.size());

        AVLNode<Integer> root = tree.getRoot();
        assertEquals((Integer) 1, root.getData());
        assertEquals(1, root.getHeight());
        assertEquals(0, root.getBalanceFactor());
        AVLNode<Integer> left = root.getLeft();
        assertEquals((Integer) 0, left.getData());
        assertEquals(0, left.getHeight());
        assertEquals(0, left.getBalanceFactor());
        AVLNode<Integer> right = root.getRight();
        assertEquals((Integer) 2, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());
    }

    @Test(timeout = TIMEOUT)
    public void testAdd() {
        // Right rotate
        /*
                2
               /
              1
             /
            0
            ->
              1
             / \
            0   2
         */

        tree.add(2);
        tree.add(1);
        tree.add(0);

        assertEquals(3, tree.size());

        AVLNode<Integer> root = tree.getRoot();
        assertEquals((Integer) 1, root.getData());
        assertEquals(1, root.getHeight());
        assertEquals(0, root.getBalanceFactor());
        AVLNode<Integer> left = root.getLeft();
        assertEquals((Integer) 0, left.getData());
        assertEquals(0, left.getHeight());
        assertEquals(0, left.getBalanceFactor());
        AVLNode<Integer> right = root.getRight();
        assertEquals((Integer) 2, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());


        // Right left rotate
        /*
            0
             \
              2
             /
            1
            ->
              1
             / \
            0   2
         */

        tree = new AVL<>();
        tree.add(0);
        tree.add(2);
        tree.add(1);

        assertEquals(3, tree.size());

        assertEquals(3, tree.size());
        root = tree.getRoot();
        assertEquals((Integer) 1, root.getData());
        assertEquals(1, root.getHeight());
        assertEquals(0, root.getBalanceFactor());
        left = root.getLeft();
        assertEquals((Integer) 0, left.getData());
        assertEquals(0, left.getHeight());
        assertEquals(0, left.getBalanceFactor());
        right = root.getRight();
        assertEquals((Integer) 2, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());
    }


    @Test(timeout = TIMEOUT)
    public void testRemove() {
        Integer temp = 1;

        /*
                  3
                /   \
              1      4
             / \
            0   2
            ->
                  3
                /   \
              2      4
             /
            0
         */


        tree.add(3);
        tree.add(temp);
        tree.add(4);
        tree.add(0);
        tree.add(2);
        assertEquals(5, tree.size());

        assertSame(temp, tree.remove(1));
        assertEquals(4, tree.size());
        AVLNode<Integer> root = tree.getRoot();
        assertEquals((Integer) 3, root.getData());
        assertEquals(2, root.getHeight());
        assertEquals(1, root.getBalanceFactor());
        AVLNode<Integer> left = root.getLeft();
        assertEquals((Integer) 2, left.getData());
        assertEquals(1, left.getHeight());
        assertEquals(1, left.getBalanceFactor());
        AVLNode<Integer> right = root.getRight();
        assertEquals((Integer) 4, right.getData());
        assertEquals(0, right.getHeight());
        assertEquals(0, right.getBalanceFactor());
        AVLNode<Integer> leftLeft = left.getLeft();
        assertEquals((Integer) 0, leftLeft.getData());
        assertEquals(0, leftLeft.getHeight());
        assertEquals(0, leftLeft.getBalanceFactor());
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        Integer temp1 = 1;
        Integer temp0 = 0;
        Integer temp2 = 2;
        Integer temp3 = 3;

        /*
               1
             /   \
            0     2
                    \
                     3
         */

        tree.add(temp1);
        tree.add(temp0);
        tree.add(temp2);
        tree.add(temp3);
        assertEquals(4, tree.size());

        // We want to make sure the data we retrieve is the one from the tree,
        // not the data that was passed in.
        assertSame(temp0, tree.get(0));
        assertSame(temp1, tree.get(1));
        assertSame(temp2, tree.get(2));
        assertSame(temp3, tree.get(3));
    }

    @Test(timeout = TIMEOUT)
    public void testContains() {
        /*
               1
             /   \
            0     2
                    \
                     3
         */

        tree.add(1);
        tree.add(0);
        tree.add(2);
        tree.add(3);
        assertEquals(4, tree.size());

        assertTrue(tree.contains(0));
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(2));
        assertTrue(tree.contains(3));
    }

    @Test(timeout = TIMEOUT)
    public void testHeight() {
        /*
                     3
                   /   \
                 1      4
                / \
               0   2
         */

        tree.add(3);
        tree.add(1);
        tree.add(4);
        tree.add(0);
        tree.add(2);

        assertEquals(2, tree.height());
    }

    @Test(timeout = TIMEOUT)
    public void testClear() {
        /*
              1
             / \
            0   2
        */

        List<Integer> toAdd = new ArrayList<>();
        toAdd.add(1);
        toAdd.add(0);
        toAdd.add(2);
        tree = new AVL<>(toAdd);

        tree.clear();
        assertEquals(0, tree.size());
        assertNull(tree.getRoot());
    }

    @Test(timeout = TIMEOUT)
    public void testElementsWithinDistance() {
        /*
                    76
                 /      \
               34        90
              /  \      /
            20    40  81
         */

        tree.add(76);
        tree.add(34);
        tree.add(90);
        tree.add(20);
        tree.add(40);
        tree.add(81);

        Set<Integer> result = new HashSet<>();
        result.add(90);

        // Should be {90}
        assertEquals(result, tree.elementsWithinDistance(90, 0));

        result.clear();
        result.add(20);
        result.add(34);
        result.add(40);
        result.add(76);

        // Should be {20, 34, 40, 76}
        assertEquals(result, tree.elementsWithinDistance(34, 1));
    }
}
