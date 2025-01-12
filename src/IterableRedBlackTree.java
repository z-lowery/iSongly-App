import java.util.*;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>> 
	extends RedBlackTree<T> implements IterableSortedCollection<T> {

    Comparable<T> max, min; // stores the maximum/minimum value for the iterator. Null if none is set. 
	
    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    public void setIteratorMin(Comparable<T> min) { 
    	this.min = min;
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     * @param max the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(Comparable<T> max) { 
    	this.max = max; 
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    public Iterator<T> iterator() {
        return new RBTIterator<>(root, this.min, this.max);
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R> implements Iterator<R> {

         // stores the start point (minimum) for the iterator
         Comparable<R> min = null;
         // stores the stop point (maximum) for the iterator
         Comparable<R> max = null;
         // stores the stack that keeps track of the inorder traversal
         Stack<BSTNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * @param root root node of the tree to traverse
         * @param min the minimum value that the iterator will return
         * @param max the maximum value that the iterator will return 
         */
        public RBTIterator(BSTNode<R> root, Comparable<R> min, Comparable<R> max) {
            // Initialize RBTIterator fields
            this.min = min;
            this.max = max;
            this.stack = new Stack<>();

            if(this.min == null) {
                BSTNode<R> node = root;
                while(node.getLeft() != null){
                    node = node.getLeft();
                }
                this.min = (Comparable<R>) node.getData();
            }

            if(this.max == null) {
                BSTNode<R> node = root;
                while(node.getRight() != null){
                    node = node.getRight();
                }
                this.max = (Comparable<R>) node.getData();
            }

            // build initial stack from the root
            buildStackHelper(root);
        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is bigger
         *   than or equal to the specified start point (maximum), and
         * - builds up the stack of ancestor nodes that contain values larger than or
         *   equal to the start point so that those nodes can be visited in the future.
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BSTNode<R> node) {
            // base case
            if(node == null) {
                return;
            }

            // recursive cases
            if (node.getData() != null && min.compareTo(node.getData()) > 0){ // node data less than min
                buildStackHelper(node.getRight());
            } else { // node data is greater than or equal to min
                stack.push(node);
                buildStackHelper(node.getLeft());
            }
        }

        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Returns the next value of the iterator.
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() {
            if(!hasNext()){
                throw new NoSuchElementException("No more elements to iterate over.");
            }

            // pop next element from the stack
            BSTNode<R> node = stack.pop();
            R value = node.getData();

            if(!(min.compareTo(value) <= 0 && max.compareTo(value) >= 0)){
                throw new NoSuchElementException("No more elements within the specified range.");
            }

            // build new stack from the right subtree of the current node
            buildStackHelper(node.getRight());

            return value;
        }
    }
}





































































































