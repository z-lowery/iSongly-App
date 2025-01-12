package iSongly;

public class BinarySearchTree <T extends Comparable<T>> implements SortedCollection<T> {

    // creates the root of the BST with no value until the insert() method is run
    protected BSTNode<T> root;

    BinarySearchTree(){
        this.root = new BSTNode<>(null);
    }

    // Tests
    public static void main(String[] args) {
        BinarySearchTree myTree = new BinarySearchTree();
        myTree.test1();
        myTree.test2();
        myTree.test3();
        //System.out.println("Test #1: " + myTree.test1() + "\nTest #2: " + myTree.test2() + "\nTest #3: " + myTree.test3());
    }
    
    /* inserts new data in the BST */
    @Override
    public void insert(T data) throws NullPointerException {
        if(data != null) {
            if(root == null){
                root = new BSTNode<>(data);
            } else {
                BSTNode<T> newNode = new BSTNode<>(data);
                BSTNode<T> currentNode = this.root;

                insertHelper(newNode, currentNode);
            }
        } else {
            throw new NullPointerException("Data attempting to be inserted is NULL!");
        }
    }

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree.  When the provided subtree
     * is null, this method does nothing.
     */
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
        if(subtree != null) {  // makes sure the method does nothing if the provided subtree is null

            /* Path variable ----------------------------------------
            used to traverse the tree, with the value changing based on the value new node being inserted and the current
            node it is being compared to during traversal. It dictates what "path" to take down the tree */
            int path;

            // var used to indicate that a new node was inserted in the tree. A further explanation is found below
            boolean newNodeInserted = false;

            /* the only instance that the subtree data would be null is if it is the root node, and it is the first
               insertion being done in the tree */
            if (subtree.getData() == null) {
                subtree.setData(newNode.getData());
                // /* debug */ System.out.println("ROOT CREATED. Value = " + subtree.getData() + "\n");
            } else {
                ///* debug  */ System.out.print(subtree.getData() + ", ");

                // returns 1 if the newNode data is greater than the currentNode data, -1 if less than, and 0 if equal
                path = newNode.data.compareTo(subtree.data);

                /* ADD A NEW NODE ----------------------------------------
                if there is a missing child node, then the program will do a check to see if the newNode data is less
                or equal to the subtree node data. If it is, then it will check to see if the child the subtree is missing
                is the left one. If so, then it sets the left child with the newNode data.

                Else, it will do the same with the right child, but instead of less than or equal to, it will see if the
                newNode data is greater than the subtree data.

                If a child is set with the newNode data, then the variable newNodeInserted is set to true, indicating a new
                node was inserted into the tree. This is so that the program does not try to traverse down the tree and
                possibly continue the recursive process of this method. In other words, the variable stops anything else
                from happening if new data is inserted into the tree (it would waste time as it is unneeded).
                 */
                if (subtree.getLeft() == null || subtree.getRight() == null) {
                    if (path <= 0 && subtree.getLeft() == null) {
                        subtree.setLeft(newNode);
                        subtree.getLeft().setUp(subtree);
                        ///* debug */ System.out.println(newNode.data + "(NL)");
                        newNodeInserted = true;
                    } else if (path > 0 && subtree.getRight() == null) {
                        subtree.setRight(newNode);
                        subtree.getRight().setUp(subtree);
                        ///* debug */ System.out.println(newNode.data + "(NR)");
                        newNodeInserted = true;
                    }
                }

                /* TRAVERSE THE TREE ----------------------------------------
                traverse down a child node if a new node was not inserted into the tree
                 */
                if (!newNodeInserted) {
                    if (path <= 0 && subtree.getLeft() != null) {
                        subtree = subtree.getLeft();
                        ///* debug */ System.out.print(subtree + "(L), ");
                        insertHelper(newNode, subtree);
                    } else if (path > 0 && subtree.getRight() != null) {
                        subtree = subtree.getRight();
                        ///* debug */ System.out.print(subtree + "(R), ");
                        insertHelper(newNode, subtree);
                    }
                }
            }
        }
    }

    /*
    checks if the given data appears as a data value in one of the nodes contained in the BST
     */
    @Override
    public boolean contains(Comparable<T> data){
        // null is nothing, and nothing can not exist. So we return false,
        if(data == null){
            return false;
        }

        int path;
        BSTNode<T> currentNode = this.root;
        ///* debug */ System.out.print("Contains method path: ");

        while(true){
            // check if current node data matches or is greater or less than the data being searched for
                path = data.compareTo(currentNode.data);
                if(path == 0){ // if matching, return true
                    return true;
                    // if greater than, traverse to the right child of the current node
                } else if (path > 0 && currentNode.getRight() != null){
                    currentNode = currentNode.getRight();
                    //System.out.print("right ");
                    // if less than, traverse to the left child of the current node
                } else if (path < 0 && currentNode.getLeft() != null){ //
                    currentNode = currentNode.getLeft();
                    //System.out.print("left ");
                    /*
                    if none of the above, then the data is not contained in the tree, so we break the loop &
                    return false
                     */
                } else {
                    break;
                }
        }
        return false;
    }

    // checks if the BST is empty by checking the root node. If the root node has data, then it is not empty.
    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    /*
    return the size of the BST using the help of the traverse() method, which uses recursion to count the
    nodes in the BST
     */
    @Override
    public int size() {
        if(this.root != null) {
            return sizeTraverse(this.root);
        } else {
            return 0;
        }
    }

    /*
    clears the BST of all data by resetting the values associated with the root to null. Because the entire tree
    relies on the root, this essentially "clears" all data from the BST.System.out.println("Test #1: " + test1());
     */
    @Override
    public void clear() {
    	this.root = null; 
    }

    /*
    returns a string of all the values in the BST in a certain order. Order is determined by the goalSelection parameter.
    Essentially, it tells the method what the "goal" of calling it is:
    0 = to level order
    1 = to in order
     */
    public String printBST(int goalSelection) throws NullPointerException{
        if(this.root != null) {
            if (goalSelection == 0) {
                return this.root.toLevelOrderString();
            } else if (goalSelection == 1) {
                return this.root.toInOrderString();
            }
        } else {
            // print nothing if the root's data is null, as the tree does not exist yet (doesn't exist if root.data == null)
            return "";
        }

        return "Invalid goalSelection parameter!\n0 = to level order " + "\n1 = to in order";
    }

    /*
    used to recursively move through all the nodes in the BST. Used by the size() method to count the nodes.
     */
    private int sizeTraverse(BSTNode<T> currentNode){
        // base case: if currentNode DNE, return 0
        if(currentNode == null){
            return 0;
        }

        // recursively count the size of left and right subtrees
        int rightSize = sizeTraverse(currentNode.getRight());
        int leftSize = sizeTraverse(currentNode.getLeft());

        // return the right subtree size + left subtree size + 1 (to count currentNode itself)
        return rightSize + leftSize + 1;
    }

    /*
    // used to recursively move through all the nodes in the BST.
    private void traverse(BSTNode<T> currentNode){
        // goes to the right child node if it exists and count/clear all their children if they exist (!= null)
        if (currentNode.getRight() != null) {
            traverse(currentNode.getRight());
        }

        if the right child node doesn't exist or had already been counted/cleared,
        check if there is a left child node. If so, go there and count/clear all its children
        if (currentNode.getLeft() != null) {
            traverse(currentNode.getLeft());
        }
    }
    */

    // TEST (1): Insert, contains, clear, & size methods (INTEGER & DOUBLE)
    public boolean test1(){
        // /* debug */ System.out.println("---------------------------------------------------------------------");
        BinarySearchTree<Integer> integerBST = new BinarySearchTree<>();
        integerBST.insert(2);
        integerBST.insert(2);
        integerBST.insert(2);
        integerBST.insert(30);
        integerBST.insert(98);
        integerBST.insert(65);
        integerBST.insert(74);
        integerBST.insert(23);
        integerBST.insert(5);
        integerBST.insert(14);
        integerBST.insert(90);
        integerBST.insert(276);
        integerBST.insert(34);
        integerBST.insert(88);
        integerBST.insert(61);
        // /* debug */ System.out.println("\nInserted 15 INTEGER nodes into the tree. Tree Size: " + tree.size() + " (Expected: 15)");
        if(!(integerBST.size() == 15)){
            return false;
        }
        // /* debug */ System.out.println("Contains 90: " + tree.contains(90) + " (Expected: true)");
        // /* debug */ System.out.println("Contains 6: " + tree.contains(6) + " (Expected: false)");
        // /* debug */ System.out.println("Contains 24: " + tree.contains(24) + " (Expected: false)");
        // /* debug */ System.out.println("Contains 34: " + tree.contains(34) + " (Expected: true)");
        if(!integerBST.contains(90) || integerBST.contains(6) || integerBST.contains(24) || !integerBST.contains(34)){
            return false;
        }
        integerBST.clear();
        // /* debug */ System.out.println("\nRan clear method. Tree Size: " + tree.size() + " (Expected: 0)");
        if(!(integerBST.size() == 0)){
            return false;
        }
        BinarySearchTree<Double> doubleBST = new BinarySearchTree<>();
        doubleBST.insert(2.4);
        doubleBST.insert(30.1);
        doubleBST.insert(98.5);
        doubleBST.insert(65.04);
        doubleBST.insert(74.9);
        doubleBST.insert(23.3);
        doubleBST.insert(5.1);
        doubleBST.insert(14.0);
        doubleBST.insert(90.4);
        doubleBST.insert(276.3);
        // /* debug */ System.out.println("Contains 14.0: " + tree.contains(14.0) + " (Expected: true)");
        // /* debug */ System.out.println("Contains 23.2: " + tree.contains(23.2) + " (Expected: false)");
        // /* debug */ System.out.println("Contains 900.0: " + tree.contains(900.0) + " (Expected: false)");
        // /* debug */ System.out.println("Contains 276.3: " + tree.contains(276.3) + " (Expected: true)");
        if(!doubleBST.contains(14.0) || doubleBST.contains(23.2) || doubleBST.contains(900.0) || !doubleBST.contains(276.3)){
            return false;
        }
        // /* debug */ System.out.println("\nInserted 10 DOUBLE nodes into the tree. Tree Size: " + tree.size() + " (Expected: 10)");
        if(!(doubleBST.size() == 10)){
            return false;
        }

        return true; // method will only return true if all other code in the test worked as expected
    }

    // TEST (2): Insert, contains, clear, & size methods (STRING & DOUBLE) + confirmation of tree shape using a string tree
    public boolean test2(){
        // /* debug */ System.out.println("---------------------------------------------------------------------");
        BinarySearchTree<String> stringBST = new BinarySearchTree<>();
        stringBST.insert("a");
        stringBST.insert("a");
        stringBST.insert("a");
        stringBST.insert("b");
        stringBST.insert("f");
        stringBST.insert("r");
        stringBST.insert("t");
        stringBST.insert("w");
        stringBST.insert("l");
        stringBST.insert("z");
        stringBST.insert("y");
        stringBST.insert("s");
        stringBST.insert("o");
        stringBST.insert("v");
        stringBST.insert("k");
        // /* debug */ System.out.println("\nInserted 15 STRING nodes into the tree. Tree Size: " + tree.size() + " (Expected: 15)");
        if(!(stringBST.size() == 15)){
            return false;
        }
        // /* debug */ System.out.println("Contains \"t\": " + tree.contains("t") + " (Expected: true)");
        // /* debug */ System.out.println("Contains \"g\": " + tree.contains("g") + " (Expected: false)");
        // /* debug */ System.out.println("Contains \"x\": " + tree.contains("x") + " (Expected: false)");
        // /* debug */ System.out.println("Contains \"w\": " + tree.contains("w") + " (Expected: true)");
        if(!stringBST.contains("t") || stringBST.contains("g") || stringBST.contains("x") || !stringBST.contains("w")){
            return false;
        }

        // test inLevelOrder
        // /* debug */ System.out.println(stringBST.printBST((0)));
        if(!stringBST.printBST(0).equals("[ " + "a, a, b, a, f, r, l, t, k, o, s, w, v, z, y" + " ]")){
            return false;
        }
        // test inOrder
        // /* debug */ System.out.println(stringBST.printBST((1)));
        if(!stringBST.printBST(1).equals("[ " + "a, a, a, b, f, k, l, o, r, s, t, v, w, y, z" + " ]")){
            return false;
        }


        stringBST.clear();
        // /* debug */ System.out.println("\nRan clear method. Tree Size: " + tree.size() + " (Expected: 0)");
        if(!(stringBST.size() == 0)){
            return false;
        }
        BinarySearchTree<Double> doubleBST = new BinarySearchTree<>();
        doubleBST.insert(2.4);
        doubleBST.insert(30.1);
        doubleBST.insert(98.5);
        doubleBST.insert(65.04);
        doubleBST.insert(74.9);
        doubleBST.insert(23.3);
        doubleBST.insert(5.1);
        doubleBST.insert(14.0);
        doubleBST.insert(90.4);
        doubleBST.insert(276.3);
        // /* debug */ System.out.println("Contains 14.0: " + tree.contains(14.0) + " (Expected: true)");
        // /* debug */ System.out.println("Contains 23.2: " + tree.contains(23.2) + " (Expected: false)");
        // /* debug */ System.out.println("Contains 900.0: " + tree.contains(900.0) + " (Expected: false)");
        // /* debug */ System.out.println("Contains 276.3: " + tree.contains(276.3) + " (Expected: true)");
        if(!doubleBST.contains(14.0) || doubleBST.contains(23.2) || doubleBST.contains(900.0) || !doubleBST.contains(276.3)){
            return false;
        }
        // /* debug */ System.out.println("\nInserted 10 DOUBLE nodes into the tree. Tree Size: " + tree.size() + " (Expected: 10)");
        if(!(doubleBST.size() == 10)){
            return false;
        }

        return true; // method will only return true if all other code in the test worked as expected
    }

    // TEST (3): Insert, clear, isEmpty (INTEGER) + confirmation of tree shape using an integer tree
    public boolean test3(){
        // /* debug */ System.out.println("---------------------------------------------------------------------");
        BinarySearchTree<Integer> integerBST = new BinarySearchTree<>();
        integerBST.insert(2);
        integerBST.insert(4);
        integerBST.insert(1);
        integerBST.insert(2);
        // /* debug */ System.out.println("\nInserted 4 INTEGER nodes into the tree. Tree Size: " + tree.size() + " (Expected: 4)");
        if(!(integerBST.size() == 4)){
            return false;
        }
        // /* debug */ System.out.println("Running 'isEmpty()' method: " + tree.isEmpty() + " (Expected: false)");
        if(integerBST.isEmpty()){
            return false;
        }
        integerBST.clear();
        // /* debug */ System.out.println("\nRan clear method. Tree Size: " + tree.size() + " (Expected: 0)");
        if(!(integerBST.size() == 0)){
            return false;
        }
        integerBST.insert(100);
        integerBST.insert(200);
        integerBST.insert(200);
        integerBST.insert(300);
        integerBST.insert(250);
        integerBST.insert(50);
        integerBST.insert(25);
        integerBST.insert(30);
        integerBST.insert(35);
        integerBST.insert(10);
        integerBST.insert(275);

        // test inLevelOrder
        if(!integerBST.printBST(0).equals("[ " + "100, 50, 200, 25, 200, 300, 10, 30, 250, 35, 275" + " ]")){
            return false;
        }
        // test inOrder
        if(!integerBST.printBST(1).equals("[ " + "10, 25, 30, 35, 50, 100, 200, 200, 250, 275, 300" + " ]")){
            return false;
        }

        return true; // method will only return true if all other code in the test worked as expected
    }

}


