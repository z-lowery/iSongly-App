package iSongly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
//import org.junit.Assert;
//import org.junit.Test;
import java.util.*;
import java.io.*;

public class BackendTests {

	/**
	 * The following test tests multiple uses of the getRange() method
	 * loading a file, including Frontend.java's implementation.
	 */
	@Test
	public void backend_IntegrationTest1(){
		IterableSortedCollection<Song> tree = new IterableRedBlackTree();
		Backend backend = new Backend(tree);
		ArrayList<String> titles = new ArrayList<>();

		// test the readData method
		try {
			backend.readData("songs.csv");
			if(!(tree.size() == 600)){
				Assertions.assertFalse(true);
			}
		} catch (IOException e){
			System.out.println(e);
		}

		// tests getRange() method
		titles.addAll(backend.getRange(-4, 0));
		Assertions.assertTrue(titles.contains("24K Magic")
				&& titles.contains("Alive")
				&& titles.contains("All About That Bass")
				&& titles.contains("All The Right Moves")
				&& titles.contains("Anything Could Happen"));
		titles.clear();
		titles.addAll(backend.getRange(-2, 0));
		Assertions.assertTrue(titles.contains("3")
				&& titles.contains("Pom Poms")
				&& titles.contains("Rich Boy")
				&& titles.contains("Starships")
				&& titles.contains("What Makes You Beautiful")
				&& !titles.contains("Alive"));
		titles.clear();

		// Frontend Implementation
		TextUITester tester = new TextUITester("g\n-5\n100\nq");
		Frontend frontend = new Frontend(new Scanner(System.in), backend);

		frontend.runCommandLoop(); // run command loop to look for user input
		String output = tester.checkOutput(); // store input
		Assertions.assertTrue(output.contains("24K Magic")
				&& output.contains("Alive")
				&& output.contains("All About That Bass")
				&& output.contains("All The Right Moves")
				&& output.contains("Anything Could Happen"));
	}

	/**
	 * The following test tests the setFilter() method, both with
	 * and without a set range. Also tests Frontend.java's implementation.
	 * */
	@Test
	public void backend_IntegrationTest2(){
		IterableSortedCollection<Song> tree = new IterableRedBlackTree();
		BackendInterface backend = new Backend(tree);
		ArrayList<String> titles = new ArrayList<>();

		try {
			backend.readData("songs.csv");
		} catch (IOException e) {
			System.out.println(e);
		}

		// setFilter() when range has already been set
		backend.getRange(-2, null); // set range
		titles.addAll(backend.setFilter(140)); // adds all 3 songs to the titles list
		Assertions.assertTrue(titles.size() == 1 && titles.contains("Pom Poms")); // confirm contents of list
		titles.clear();

		// setFilter() when no range is set
		backend.getRange(null, null); // reset range
		backend.setFilter(null);
		titles.addAll(backend.setFilter(140)); // should add only the song that fits in the range and filter
		Assertions.assertTrue(titles.contains("Rude")
				&& titles.contains("Stitches")
				&& titles.contains("Locked Out of Heaven")
				&& !titles.contains("Starships")
				&& titles.contains("Antisocial (with Travis Scott)")
				&& titles.contains("Mercy"));

		// Frontend Implementation
		TextUITester tester = new TextUITester("g\n-2\n999\nf\n140\nd\nq");
		Frontend frontend = new Frontend(new Scanner(System.in), backend);

		frontend.runCommandLoop(); // run command loop to look for user input
		String output = tester.checkOutput(); // store input
		String normalizedOutput = output.replaceAll("\\R", "\n"); // normalize input
		Assertions.assertTrue(normalizedOutput.contains("The five most Danceable songs are:\n" +
				"- Pom Poms\n" +
				"Welcome to iSongly!\n" +
				"Please select an option:\n" +
				"[L]oad Song File\n" +
				"[G]et Songs by Loudness\n" +
				"[F]ilter Songs by Speed\n" +
				"[D]isplay five most Danceable\n" +
				"[Q]uit"));
	}

	/**
	 * The following test tests that the fiveMost() method works as
	 * expected and that when no file has been loaded, no error is
	 * printed. Also tests Frontends iml
	 */
	@Test
	public void backend_IntegrationTest3(){
		IterableSortedCollection<Song> tree = new IterableRedBlackTree();
		BackendInterface backend = new Backend(tree);
		ArrayList<String> titles = new ArrayList<>();

		/** tests that when no file has been loaded, no error is printed. */

		/** getRange() */
		titles.addAll(backend.getRange(-4, 0));
		Assertions.assertTrue(titles.isEmpty());

		/** setFilter() */
		titles.addAll(backend.setFilter(120));
		Assertions.assertTrue(titles.isEmpty());

		/** fiveMost() */
		titles.addAll(backend.fiveMost());
		Assertions.assertTrue(titles.isEmpty());

		// Frontend Implementation
		TextUITester tester = new TextUITester("g\n-4\n0\nf\n120\nd\nq");
		Frontend frontend = new Frontend(new Scanner(System.in), backend);

		frontend.runCommandLoop(); // run command loop to look for user input
		String output = tester.checkOutput(); // store input
		String normalizedOutput = output.replaceAll("\\R", "\n"); // normalize input
		Assertions.assertTrue(normalizedOutput.contains("You must load a file before any songs can be printed"));

		try {
			backend.readData("songs.csv");
		} catch (IOException e) {
			System.out.println(e);
		}

		// reset range and filter
		backend.getRange(null,null);
		backend.setFilter(null);

		/** tests fiveMost() with no range or filter set */
		titles.addAll(backend.fiveMost());
		Assertions.assertTrue(titles.contains("Bad Liar")
				&& titles.contains("Drip (feat. Migos)")
				&& titles.contains("Anaconda")
				&& titles.contains("Bodak Yellow")
				&& titles.contains("Come Get It Bae"));

		/** tests fiveMost() with a range and filter set */
		titles.clear(); // clear the list for the new test
		backend.getRange(-5, 100); // set range
		backend.setFilter(120); // set filter
		titles.addAll(backend.fiveMost());
		Assertions.assertTrue(titles.contains("There's Nothing Holdin' Me Back")
				&& titles.contains("Move")
				&& titles.contains("Sparks")
				&& titles.contains("That's What I Like")
				&& titles.contains("Sucker"));


	}

	/**
	 * Tests methods in Frontend.java
	 * 		1) loading a file: songs.csv
	 * 		2) setting range: min = -5 and max = 0
	 * 		3) setting filter: 120
	 * 		4) displaying top five most danceable
	 * 				- Check output (this should be the
	 * 				  same as previous test)
	 * 		4) quitting
	 */
	@Test
	public void backend_IntegrationTest4(){
		TextUITester tester = new TextUITester("L\nsongs.csv\ng\n-5\n100\nf\n120\nd\nq");
		IterableSortedCollection<Song> tree = new IterableRedBlackTree<>();
		BackendInterface backend = new Backend(tree);
		Frontend frontend = new Frontend(new Scanner(System.in), backend);
		ArrayList<String> titles = new ArrayList<>();

		frontend.runCommandLoop(); // run command loop to look for user input
		String output = tester.checkOutput(); // store input
		String normalizedOutput = output.replaceAll("\\R", "\n"); // normalize input
		Assertions.assertTrue(normalizedOutput.contains( // make sure the end of the output is as expected
				"- There's Nothing Holdin' Me Back\n" +
						"- Move\n" +
						"- Sparks\n" +
						"- That's What I Like\n" +
						"- Sucker\n" +
						"Welcome to iSongly!\n" +
						"Please select an option:\n" +
						"[L]oad Song File\n" +
						"[G]et Songs by Loudness\n" +
						"[F]ilter Songs by Speed\n" +
						"[D]isplay five most Danceable\n" +
						"[Q]uit"));
	}



	/**
	 * The following test tests multiple uses of the getRange() method
	 * loading a file.
	 */
	@Test
	public void backendTest1(){
		IterableSortedCollection<Song> tree = new IterableRedBlackTree();
		BackendInterface backend = new Backend(tree);
		ArrayList<String> titles = new ArrayList<>();

		// test the readData method
		try {
			backend.readData("songs.csv");
			if(!(tree.size() == 600)){
				Assertions.assertFalse(true);
			}
		} catch (IOException e){
			System.out.println(e);
		}

		// tests getRange() method
		titles.addAll(backend.getRange(-4, 0));
		Assertions.assertTrue(titles.contains("24K Magic")
				&& titles.contains("Alive")
				&& titles.contains("All About That Bass")
				&& titles.contains("All The Right Moves")
				&& titles.contains("Anything Could Happen"));
		titles.clear();
		titles.addAll(backend.getRange(-2, 0));
		Assertions.assertTrue(titles.contains("3")
				&& titles.contains("Pom Poms")
				&& titles.contains("Rich Boy")
				&& titles.contains("Starships")
				&& titles.contains("What Makes You Beautiful")
				&& !titles.contains("Alive"));
		titles.clear();
		Assertions.assertTrue(true);
	}

	/**
	 * The following test tests the setFilter() method, both with
	 * and without a set range.
	 * */
	@Test
	public void backendTest2(){
		IterableSortedCollection<Song> tree = new IterableRedBlackTree();
		BackendInterface backend = new Backend(tree);
		ArrayList<String> titles = new ArrayList<>();

		try {
			backend.readData("songs.csv");
		} catch (IOException e) {
			System.out.println(e);
		}

		// setFilter() when range has already been set
		backend.getRange(-2, null); // set range
		titles.addAll(backend.setFilter(140)); // adds all 3 songs to the titles list
		Assertions.assertTrue(titles.size() == 1 && titles.contains("Pom Poms")); // confirm contents of list
		titles.clear();

		// setFilter() when no range is set
		backend.getRange(null, null); // reset range
		backend.setFilter(null);
		titles.addAll(backend.setFilter(140)); // should add only the song that fits in the range and filter
		Assertions.assertTrue(titles.contains("Rude")
				&& titles.contains("Stitches")
				&& titles.contains("Locked Out of Heaven")
				&& !titles.contains("Starships")
				&& titles.contains("Antisocial (with Travis Scott)")
				&& titles.contains("Mercy"));

		Assertions.assertTrue(true);
	}

	/**
	 * The following test tests that the fiveMost() method works as
	 * expected.
	 */
	@Test
	public void backendTest3(){
		IterableSortedCollection<Song> tree = new IterableRedBlackTree();
		BackendInterface backend = new Backend(tree);
		ArrayList<String> titles = new ArrayList<>();

		try {
			backend.readData("songs.csv");
		} catch (IOException e) {
			System.out.println(e);
		}

		/** tests fiveMost() with no range or filter set */
		titles.addAll(backend.fiveMost());
		Assertions.assertTrue(titles.contains("Bad Liar")
				&& titles.contains("Drip (feat. Migos)")
				&& titles.contains("Anaconda")
				&& titles.contains("Bodak Yellow")
				&& titles.contains("Come Get It Bae"));

		/** tests fiveMost() with a range and filter set */
		titles.clear(); // clear the list for the new test
		backend.getRange(-5, 100); // set range
		backend.setFilter(120); // set filter
		titles.addAll(backend.fiveMost());
		Assertions.assertTrue(titles.contains("There's Nothing Holdin' Me Back")
				&& titles.contains("Move")
				&& titles.contains("Sparks")
				&& titles.contains("That's What I Like")
				&& titles.contains("Sucker"));

		Assertions.assertTrue(true);
	}
}








































































































































































































































































































































































































































































