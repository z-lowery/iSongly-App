import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;

public class FrontendTests {

    /**
     * Checks if the frontend correctly loads a file and displays the main menu
     * Simulates user input to load a file and then quit the application
     */
    @Test
    public void frontendTest1() {
        TextUITester tester = new TextUITester("L\ntest.txt\nQ\n");
        
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        BackendInterface backendPlaceholder = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(System.in), backendPlaceholder);
        frontend.runCommandLoop();
        
        String output = tester.checkOutput();
        
        Assertions.assertTrue(output.contains("Welcome to iSongly!"));
        Assertions.assertTrue(output.contains("Enter the filename to load:"));
        Assertions.assertTrue(output.contains("Data loaded successfully."));
    }

    /**
     * Verifies if the frontend correctly handles the 'G' option
     * Simulates user input to select 'G', enter loudness range, and then quit
     */
    @Test
    public void frontendTest2() {
        TextUITester tester = new TextUITester("G\n1\n10\nQ\n");
        
        IterableSortedCollection<Song> tree = new Tree_Placeholder();         
        BackendInterface backendPlaceholder = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(System.in), backendPlaceholder);
        frontend.runCommandLoop();
        
        String output = tester.checkOutput();
        
        Assertions.assertTrue(output.contains("Enter the minimum Loudness:"));
        Assertions.assertTrue(output.contains("Enter the maximum Loudness:"));
        Assertions.assertTrue(output.contains("Songs by Loudness:"));
        Assertions.assertTrue(output.contains("BO$$"));
        Assertions.assertTrue(output.contains("Cake By The Ocean"));
    }

    /**
     * Checks if the frontend correctly handles the 'F' and 'D' options. 
     * Simulates setting a filter and then displaying the top five songs.
     */
    @Test
    public void frontendTest3() {
        TextUITester tester = new TextUITester("F\n50\nD\nQ\n");

        IterableSortedCollection<Song> tree = new Tree_Placeholder();         
        BackendInterface backendPlaceholder = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(System.in), backendPlaceholder);
        frontend.runCommandLoop();
        
        String output = tester.checkOutput();
        
        Assertions.assertTrue(output.contains("Enter the minimum Speed threshold"));
        Assertions.assertTrue(output.contains("Filter set successfully."));
        Assertions.assertTrue(output.contains("The five most Danceable songs are:"));
        Assertions.assertTrue(output.contains("A L I E N S"));
        Assertions.assertTrue(output.contains("BO$$"));
        Assertions.assertTrue(output.contains("Cake By The Ocean"));
    }
}