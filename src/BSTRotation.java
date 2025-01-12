package iSongly;

import java.lang.Comparable;

public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child  is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException     when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *                                  nodes are not initially (pre-rotation) related that way
     */
    protected void rotate(BSTNode<T> child, BSTNode<T> parent)
            throws NullPointerException, IllegalArgumentException {
        // if the parameters passed is NOT null and the relationship is as expected (child node's parent is the parent node)
        if (!(child == null || parent == null) && child.getUp() == parent) {
            int path = child.data.compareTo(parent.data);
            // child is the left child, perform right rotation
            if (path <= 0) {
                ///* debug */ System.out.println("Child is right of the parent. Performed a RIGHT rotation");
                rightRotation(child, parent);

                // child is the right child, perform left rotation
            } else {
                ///* debug */ System.out.println("Child is right of the parent. Performed a LEFT rotation");
                leftRotation(child, parent);
            }
        } else {
            // if one of the parameters passed is null
            if (child == null || parent == null) {
                throw new NullPointerException("A parameter in rotate() is null!");
                // the passed child node is not the child of the passed parent node
            } else {
                throw new IllegalArgumentException("Provided child and parent nodes are not related in that way!");
            }
        }
    }

    public void leftRotation(BSTNode<T> child, BSTNode<T> parent) {
        // grab the parent of the parent node. For explanation purposes, we will call this the "grandparent"
        BSTNode<T> grandparent = parent.getUp();

        // if grandparent = null, then the parent is the root meaning the root of the tree will change to the child.
        if (grandparent == null) {
            root = child;
            child.setUp(null); // set child's parent to null since it becomes the root
        } else {
            // switch the positions of the child and parent in the tree by:
            child.setUp(grandparent); // setting the child's parent to be the grandparent
           if(child.getData().compareTo(grandparent.getData()) <= 0){
                grandparent.setLeft(child);
            } else {
                grandparent.setRight(child); // setting grandparent's right node to be the child
            }
        }

        // switch child's left node to the right node of parent
        parent.setRight(child.getLeft()); // set parent's right child to be the left child of child
        if (parent.getRight() != null) {
            parent.getRight().setUp(parent); // set parent of that child to reflect the change
        }

        parent.setUp(child); // set the parent's parent to be the child
        child.setLeft(parent); // set the child's left node to be the parent
        /*
        System.out.println(" " +
                " Grandparent: \n\tData = " + grandparent.getData() + "\n\tLeft = " + grandparent.getLeft() + "\n\tRight = " + grandparent.getRight() + "\n\tUp = " + grandparent.getUp() +
                " \nParent: \n\tData = " + parent.getData() + "\n\tLeft = " + parent.getLeft() + "\n\tRight = " + parent.getRight() + "\n\tUp = " + parent.getUp() +
                " \nChild: \n\tData = " + child.getData() + "\n\tLeft = " + child.getLeft() + "\n\tRight = " + child.getRight() + "\n\tUp = " + child.getUp());
         */
    }

    public void rightRotation(BSTNode<T> child, BSTNode<T> parent) {
        // grab the parent of the parent node. For explanation purposes, we will call this the "grandparent"
        BSTNode<T> grandparent = parent.getUp();

        // if grandparent = null, then the parent is the root meaning the root of the tree will change to the child.
        if (grandparent == null) {
            root = child;
            child.setUp(null);
        } else {
            // switch the positions of the child and parent in the tree by:
            child.setUp(grandparent); // setting the child's parent to be the grandparent
            if(child.getData().compareTo(grandparent.getData()) <= 0){
                grandparent.setLeft(child);
            } else {
                grandparent.setRight(child); // setting grandparent's right node to be the child
            }
        }

        // switch child's left node to the right node of parent
        parent.setLeft(child.getRight()); // set parent's left child to be the right child of child
        if (parent.getLeft() != null) {
            parent.getLeft().setUp(parent); // set parent of that child to reflect the change
        }

        parent.setUp(child); // set the parent's parent to be the child
        child.setRight(parent); // set the child's right node to be the parent
    }

    public BSTRotation<Integer> grabIntegerTree(boolean left) {
        BSTRotation<Integer> integerTree = new BSTRotation<>();
        if (left) {
            integerTree.insert(500);
            integerTree.insert(250);
            integerTree.insert(750);
            integerTree.insert(600);
            integerTree.insert(1000);
            integerTree.insert(800);
            integerTree.insert(2000);
        } else {
            integerTree.insert(500);
            integerTree.insert(400);
            integerTree.insert(600);
            integerTree.insert(300);
            integerTree.insert(450);
            integerTree.insert(200);
            integerTree.insert(350);
        }
        return integerTree;
    }

    // This test focuses on LEFT rotations of an Integer tree. One left rotation w/o root as the parent, and one with
    // There are 3 children between them
    public boolean test1() {
        // ---------- left rotation test w/o root as the parent. Both the parent and the child have 2 children ---------- //
        // creates a sample tree
        BSTRotation<Integer> integerTree = grabIntegerTree(true);

        // set parent and child node
        BSTNode<Integer> parent = integerTree.root.getRight();
        BSTNode<Integer> child = parent.getRight();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 750 || child.getData() != 1000) {
            return false;
        }
        // test the rotate method. We are expecting a LEFT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "500, 250, 1000, 750, 2000, 600, 800" + " ]")) {
            return false;
        }

        // ---------- left rotation test w/ root as the parent. Both the parent and the child have 2 children ---------- //
        // resets previous tree.
        integerTree = grabIntegerTree(true);

        // set parent and child node
        parent = integerTree.root;
        child = parent.getRight();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 500 || child.getData() != 750) {
            return false;
        }
        // test the rotate method. We are expecting a LEFT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "750, 500, 1000, 250, 600, 800, 2000" + " ]")) {
            return false;
        }

        return true;
    }

    // This test focuses on RIGHT rotations of an Integer tree. One right rotation w/o root as the parent, and one with
    // There are 3 children between them
    public boolean test2() {
        // ---------- right rotation test w/o root as the parent. Both the parent and the child have 2 children ---------- //
        // creates a sample tree
        BSTRotation<Integer> integerTree = grabIntegerTree(false);

        // set parent and child node
        BSTNode<Integer> parent = integerTree.root.getLeft();
        BSTNode<Integer> child = parent.getLeft();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 400 || child.getData() != 300) {
            return false;
        }
        // test the rotate method. We are expecting a RIGHT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "500, 300, 600, 200, 400, 350, 450" + " ]")) {
            return false;
        }

        // ---------- right rotation test w/ root as the parent. Both the parent and the child have 2 children ---------- //
        // resets previous tree.
        integerTree = grabIntegerTree(false);

        // set parent and child node
        parent = integerTree.root;
        child = parent.getLeft();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 500 || child.getData() != 400) {
            return false;
        }
        // test the rotate method. We are expecting a LEFT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "400, 300, 500, 200, 350, 450, 600" + " ]")) {
            return false;
        }

        return true;
    }

    // This test focuses on both left and right rotations of an Integer tree with a variable amount of children for the
    // parent and child node being rotated.
    public boolean test3() {
        // 2 children between the parent and child node. Parent is the root node.
        BSTRotation<Integer> integerTree = new BSTRotation<>();
        integerTree.insert(500);
        integerTree.insert(250);
        integerTree.insert(750);
        integerTree.insert(1000);
        integerTree.insert(400);
        integerTree.insert(100);

        // set parent and child node
        BSTNode<Integer> parent = integerTree.root;
        BSTNode<Integer> child = parent.getRight();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 500 || child.getData() != 750) {
            return false;
        }
        // test the rotate method. We are expecting a LEFT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "750, 500, 1000, 250, 100, 400" + " ]")) {
            return false;
        }

        // 1 child between the parent and child node. Parent is the root node.
        integerTree = new BSTRotation<>();
        integerTree.insert(500);
        integerTree.insert(250);
        integerTree.insert(750);

        // set parent and child node
        parent = integerTree.root;
        child = parent.getRight();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 500 || child.getData() != 750) {
            return false;
        }
        // test the rotate method. We are expecting a LEFT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "750, 500, 250" + " ]")) {
            return false;
        }

        // NO children between the parent and child node. Parent is the root node.
        integerTree = new BSTRotation<>();
        integerTree.insert(500);
        integerTree.insert(250);

        // set parent and child node
        parent = integerTree.root;
        child = parent.getLeft();
        // verify that the parent and child node have the expected data values
        if (parent.getData() != 500 || child.getData() != 250) {
            return false;
        }
        // test the rotate method. We are expecting a LEFT rotation
        integerTree.rotate(child, parent);
        // /* debug */ System.out.println(integerTree.root.toLevelOrderString());
        if (!integerTree.root.toLevelOrderString().equals("[ " + "250, 500" + " ]")) {
            return false;
        }

        return true;
    }
}













































































































































































































































































































































































































































































































































































































