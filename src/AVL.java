import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
 *
 * @author Dean Hiromasa Miyata-Dawson
 * @version 1.0
 * @userid ddawson42
 * @GTID 903833148
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Error: data or an element in data is null.");
        }
        for (T i : data) {
            if (i == null) {
                throw new IllegalArgumentException("Error: data or an element in data is null.");
            }
            this.add(i);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add data of null value to tree.");
        }
        if (root == null) {
            root = new AVLNode<T>(data);
            updateHeightBalanceFactor(root);
            size++;
        } else {
            root = addHelper(data, root);
            size++;
        }
    }

    /**
     * Helper method for add()
     *
     * @param data data to be added to tree
     * @param node current node in traversal
     * @return balanced tree with new node added
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> node) {
        if (node == null) {
            return new AVLNode<T>(data);
        }
        int compare = data.compareTo(node.getData());
        if (compare == 0) {
            size--;
            return node;
        }
        if (compare > 0) {
            node.setRight(addHelper(data, node.getRight()));
        } else if (compare < 0) {
            node.setLeft(addHelper(data, node.getLeft()));
        }
        updateHeightBalanceFactor(node);
        return balance(node);
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data, NOT predecessor. As a reminder, rotations can occur
     * after removing the successor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove 'null' data.");
        }
        if (!contains(data)) {
            throw new NoSuchElementException("Element does not exist in BST.");
        }
        T removed = get(data);
        root = removeHelper(data, root);
        size--;
        return removed;
    }

    /**
     * Helper methdo for remove()
     *
     * @param data data to remove
     * @param node current node in traversal
     * @return AVL with removed data
     */
    private AVLNode<T> removeHelper(T data, AVLNode<T> node) {
        if (node == null) {
            return null;
        }
        int compare = data.compareTo(node.getData());
        if (compare < 0) {
            node.setLeft(removeHelper(data, node.getLeft()));
        } else if (compare > 0) {
            node.setRight(removeHelper(data, node.getRight()));
        } else {
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            } else {
                node.setData(getSuccessor(node.getRight()));
                node.setRight(removeHelper(node.getData(), node.getRight()));
            }
        }
        updateHeightBalanceFactor(node);
        return balance(node);
    }

    /**
     * Helper method for removeHelper()
     * Finds successor to node getting removed
     *
     * @param node right child node of node getting removed
     * @return successor of node getting removed
     */
    private T getSuccessor(AVLNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node.getData();
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (root == null) {
            throw new NoSuchElementException("No element available for search.");
        }
        T i = getHelper(data, root);
        if (i != null) {
            return i;
        } else {
            throw new NoSuchElementException("Element does not exist in BST.");
        }
    }

    /**
     * Helper method for get()
     *
     * @param data data to search for
     * @param node current node in traversal
     * @return data being searched for
     */
    private T getHelper(T data, AVLNode<T> node) {
        int compare = data.compareTo(node.getData());
        if (compare >= 1) {
            if (node.getRight() == null) {
                return null;
            } else {
                return getHelper(data, node.getRight());
            }
        } else if (compare <= -1) {
            if (node.getLeft() == null) {
                return null;
            } else {
                return getHelper(data, node.getLeft());
            }
        } else {
            return node.getData();
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        }
        if (root == null) {
            return false;
        }
        return (getHelper(data, root) != null);
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    /**
     * Helper method for height()
     *
     * @param node node to get height for
     * @return height of node
     */
    private int heightHelper(AVLNode<T> node) {
        if (node == null) {
            return -1;
        } else {
            return 1 + Math.max(heightHelper(node.getLeft()), heightHelper(node.getRight()));
        }
    }

    /**
     * Clears the tree
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Helper method to update height and balance factor of node
     *
     * @param node node to update height and balance factor of
     */
    private void updateHeightBalanceFactor(AVLNode<T> node) {
        node.setBalanceFactor(easyHeight(node.getLeft()) - easyHeight(node.getRight()));
        node.setHeight(1 + Math.max(easyHeight(node.getLeft()), easyHeight(node.getRight())));
    }

    /**
     * Helper method to find height of a node
     *
     * @param node node to get height of
     * @return height of node
     */
    private int easyHeight(AVLNode<T> node) {
        if (node == null) {
            return -1;
        } else {
            return node.getHeight();
        }
    }

    /**
     * Helper method to balance AVL
     * @param node node to be balanced
     * @return balanced subtree for node
     */
    private AVLNode<T> balance(AVLNode<T> node) {
        if (node.getBalanceFactor() < -1) {
            if (node.getRight().getBalanceFactor() > 0) {
                node.setRight(rotateRight(node.getRight()));
            }
            node = rotateLeft(node);
        } else if (node.getBalanceFactor() > 1) {
            if (node.getLeft().getBalanceFactor() < 0) {
                node.setLeft(rotateLeft(node.getLeft()));
            }
            node = rotateRight(node);
        }
        return node;
    }

    /**
     * Helper method to make right rotation
     *
     * @param node node to perform right rotation on
     * @return right-rotated subtree
     */
    private AVLNode<T> rotateRight(AVLNode<T> node) {
        AVLNode<T> leftNode = node.getLeft();
        node.setLeft(leftNode.getRight());
        leftNode.setRight(node);
        updateHeightBalanceFactor(node);
        updateHeightBalanceFactor(leftNode);
        return leftNode;
    }

    /**
     * Helper method to make left rotation
     *
     * @param node node to perform left rotation on
     * @return left-rotated subtree
     */
    private AVLNode<T> rotateLeft(AVLNode<T> node) {
        AVLNode<T> rightNode = node.getRight();
        node.setRight(rightNode.getLeft());
        rightNode.setLeft(node);
        updateHeightBalanceFactor(node);
        updateHeightBalanceFactor(rightNode);
        return rightNode;
    }

    /**
     * Find all elements within a certain distance from the given data.
     * "Distance" means the number of edges between two nodes in the tree.
     *
     * To do this, first find the data in the tree. Keep track of the distance
     * of the current node on the path to the data (you can use the return
     * value of a helper method to denote the current distance to the target
     * data - but note that you must find the data first before you can
     * calculate this information). After you have found the data, you should
     * know the distance of each node on the path to the data. With that
     * information, you can determine how much farther away you can traverse
     * from the main path while remaining within distance of the target data.
     *
     * Use a HashSet as the Set you return. Keep in mind that since it is a
     * Set, you do not have to worry about any specific order in the Set.
     *
     * This must be implemented recursively.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *              50
     *            /    \
     *         25      75
     *        /  \     / \
     *      13   37  70  80
     *    /  \    \      \
     *   12  15    40    85
     *  /
     * 10
     * elementsWithinDistance(37, 3) should return the set {12, 13, 15, 25,
     * 37, 40, 50, 75}
     * elementsWithinDistance(85, 2) should return the set {75, 80, 85}
     * elementsWithinDistance(13, 1) should return the set {12, 13, 15, 25}
     *
     * @param data     the data to begin calculating distance from
     * @param distance the maximum distance allowed
     * @return the set of all data within a certain distance from the given data
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   is the data is not in the tree
     * @throws java.lang.IllegalArgumentException if distance is negative
     */
    public Set<T> elementsWithinDistance(T data, int distance) {
        if (data == null) {
            throw new IllegalArgumentException("Error: data is null.");
        }
        if (!contains(data)) {
            throw new NoSuchElementException("Error: data is not in tree.");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("Error: distance cannot be negative");
        }
        HashSet<T> set = new HashSet<>();
        elementsWithinDistanceHelper(data, distance, root, set);
        return set;
    }


    /**
     * Helper method for elementsWithinDistance()
     *
     * @param data data to begin calculating distance from
     * @param maxDistance maximum distance allowed
     * @param curr current node in traversal
     * @param set Hash Set of all elements with given distance
     * @return current distance away from data
     */
    private int elementsWithinDistanceHelper(T data, int maxDistance, AVLNode<T> curr, Set<T> set) {
        int compare = data.compareTo(curr.getData());
        int childDistance = -1;
        if (compare > 0) {
            childDistance = elementsWithinDistanceHelper(data, maxDistance, curr.getRight(), set);
        } else if (compare < 0) {
            childDistance = elementsWithinDistanceHelper(data, maxDistance, curr.getLeft(), set);
        }

        int currDistance = childDistance + 1;
        if (currDistance <= maxDistance) {
            set.add(curr.getData());
        }
        if (currDistance < maxDistance) {
            if (curr.getLeft() != null) {
                elementsWithinDistanceBelow(curr.getLeft(), maxDistance, currDistance + 1, set);
            }
            if (curr.getRight() != null) {
                elementsWithinDistanceBelow(curr.getRight(), maxDistance, currDistance + 1, set);
            }
        }
        return currDistance;
    }

    /**
     * Helper method for elementsWithinDistanceHelper()
     * Adds data to set if currDistance <= maxDistance
     * Recursive call on curr's children add them to set if child's distance <= maxDistance
     *
     * @param curr current node in traversal
     * @param maxDistance maximum distance allowed
     * @param currDistance distance between current node and target node
     * @param set current Hash Set of data with maxDistance
     */
    private void elementsWithinDistanceBelow(AVLNode<T> curr, int maxDistance, int currDistance, Set<T> set) {
        if (currDistance <= maxDistance) {
            set.add(curr.getData());
        }
        if (currDistance < maxDistance) {
            if (curr.getLeft() != null) {
                elementsWithinDistanceBelow(curr.getLeft(), maxDistance, currDistance + 1, set);
            }
            if (curr.getRight() != null) {
                elementsWithinDistanceBelow(curr.getRight(), maxDistance, currDistance + 1, set);
            }
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
