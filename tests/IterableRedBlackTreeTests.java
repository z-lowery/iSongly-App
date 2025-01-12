import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;  

public class IterableRedBlackTreeTests {
  
    /**
     * This test tests the following scenarios:
     *      - An integer tree
     *      - A tree with a specified start(min) and end point(max)
     *      - A tree without duplicates
     */
    @Test
    public void IterableRBTTest01() {
        RedBlackTree<Integer> tree = newIntRedBlackTree(); // initializes new tree
        RBTIterator<Integer> iterator = new RBTIterator<>(tree.root, 6, 22); // initializes new iterator and builds stack
        String result = ""; // stores result of poping all values between min and max from the built stack

        result = getResult((Iterator<T>) iterator); // stores values between min and max from the built stack
        Assertions.assertTrue(result.equals("6 8 11 13 15 17 22 ")); // assertion statement to confirm results

    }

    public RedBlackTree<Integer> newIntRedBlackTree(){
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(1);
        tree.insert(6);
        tree.insert(8);
        tree.insert(11);
        tree.insert(13);
        tree.insert(17);
        tree.insert(15);
        tree.insert(25);
        tree.insert(22);
        tree.insert(27);
        return tree;
    }

    /**
     * This test tests the following scenarios:
     *      - A string tree
     *      - A tree with a specified start(min) and an UNspecified end point(max)
     */
    @Test
    public void IterableRBTTest02() {
        RedBlackTree<String> tree = new RedBlackTree<>(); // initialize new tree

        tree.insert("a");
        tree.insert("b");
        tree.insert("e");
        tree.insert("d");
        tree.insert("r");
        tree.insert("z");
        tree.insert("g");
        tree.insert("u");
        tree.insert("p");
        tree.insert("k");
        RBTIterator<String> iterator = new RBTIterator<>(tree.root, "h", null); // initializes new iterator and builds stack

        String result = getResult((Iterator<T>) iterator);
        Assertions.assertTrue(result.equals("k p r u z ")); // assertion statement to confirm results


    }

    /**
     * This test tests the following scenarios:
     *      - An integer tree
     *      - A tree with an UNspecified start(min) and a specified end point(max)
     *      - A tree with duplicates
     */
    @Test
    public void IterableRBTTest03() {
        RedBlackTree<Integer> tree = newIntRedBlackTree(); // initializes new tree
        RBTIterator<Integer> iterator = new RBTIterator<>(tree.root, null, 22); // initializes new iterator and builds stack
        String result = ""; // stores result of poping all values between min and max from the built stack

        tree.insert(13); // insert duplicate into the tree

        result = getResult((Iterator<T>) iterator); // stores values between min and max from the built stack
        //System.out.println( result);

        Assertions.assertTrue(result.equals("1 6 8 11 13 13 15 17 22 ")); // assertion statement to confirm results

    }

    public String getResult(Iterator<T> iterator){
        StringBuilder result = new StringBuilder();

        try {
            while(iterator().hasNext()){
                result.append(iterator.next());
                result.append(" ");
            }
        } catch (NoSuchElementException e){
        }

        return result.toString();
    }
}