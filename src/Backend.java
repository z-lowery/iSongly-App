package iSongly;

// Last updated: 10/23/2024
import java.io.*;
import java.util.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Assertions;

public class Backend implements BackendInterface {

	// fields for the backend //
    private IterableSortedCollection<Song> backend_tree = null;

    private boolean rangeSet = false;
    private Integer rangeLow = null;
    private Integer rangeHigh = null;

	private boolean fileLoaded = false;
    private Integer speedThreshold = null;

	// backend constructor
    public Backend(IterableSortedCollection<Song> tree){
		this.backend_tree = tree;
	}

    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided songs.txt, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other song qualities.
     * After reading songs from the file, the songs are inserted into
     * the tree passed to the constructor.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
     public void readData(String filename) throws IOException {
	    try {
			Scanner sc = new Scanner(new File(filename));

			/* counts the number of columns the data has by using the header of the file */
			ArrayList<String> colArray = new ArrayList<String>(); // array that stores the names of each column

			if(sc.hasNextLine()){
				String colName = ""; // var that stores column name
				String header = sc.nextLine(); // the header is expected to be the first line of the file
				///** debug */ System.out.println("File header = " + header);
				char[] charArray = header.toCharArray(); // coverts header string to an array of characters

				for(int i = 0; i < charArray.length; i++){ // iterates through the array of characters to find commas that seperate columns
					char c = charArray[i];
					if(c == ','){ // if a comma is found, increment colCount
						colArray.add(colName);
						colName = "";
					} else {
						colName += c;
					}
				}
				colArray.add(colName); // same logic here
				///** debug */ System.out.println("# of columns from header: " + colCount);
			} else{
				throw new IOException("No text in the provided file!"); // throws an IOException if the file does not have any text
			}

			///** debug */ for(int i = 1; i < colArray.size(); i++){System.out.print(colArray.get(i) + " ");}; System.out.println();

			/** check that the required fields for a song object is in the header of the file. if one is not, throw an IOException */
			if(!colArray.contains("title") || !colArray.contains("artist") || !colArray.contains("top genre")
					|| !colArray.contains("year") || !colArray.contains("bpm") || !colArray.contains("nrgy")
					|| !colArray.contains("dnce") || !colArray.contains("dB") || !colArray.contains("live")){
				   throw new IOException("A field needed to instantiate a song object was not found in the column header of the privided file!");
			}


			/**
			 * moves through the file line by line to create an equal number
			 * of objects that stores various points of data determined by
			 * colCount.
			*/
			while(sc.hasNextLine()){
				String songData = sc.nextLine();

				/** create variables to store values for the fields in the song class. */
				String title, artist, genres; // note: 3 strings
				int year, bpm, energy, danceability, loudness, liveness; // note: 6 ints
				Comparator<Song> comparator;

				// title, artist, top genre, year, bpm, nrgy, dnce, dB, live, val, dur, acous, spch, pop

				// get title, artist, 'top genre', year, bpm, energy, danceability, loudness, and liveness
				title = findStringData(colArray.indexOf("title"), songData);

				///** debug */ System.out.println("Song title = " + title);
				artist = findStringData(colArray.indexOf("artist"), songData);
				///** debug */ System.out.println("artist = " + artist);
				genres = findStringData(colArray.indexOf("top genre"), songData);
				///** debug */ System.out.println("Genre = " + genres);
				year = findIntData(colArray.indexOf("year"), songData);
				//System.out.println("Song year = " + year);
				bpm = findIntData(colArray.indexOf("bpm"),songData);
				energy = findIntData(colArray.indexOf("nrgy"),songData);
				danceability = findIntData(colArray.indexOf("dnce"),songData);
				loudness = findIntData(colArray.indexOf("dB"),songData);
				liveness = findIntData(colArray.indexOf("live"),songData);

				if(title.isEmpty() || artist.isEmpty() || genres.isEmpty()){
					throw new IOException("Data provided for a title, artist, or genre field is empty!");
				}

				// instantiate a new song object and add it to the tree
				Song song = new Song(title, artist, genres, year, bpm, energy, danceability, loudness, liveness);
				backend_tree.insert(song);

				/*
				System.out.println("" +
				//		"Song title: " + title +
				//		"\n\tArtist: " + artist +
				//		"\n\tGenre: " + genres +
				//		"\n\tYear: " + year +
				//		"\n\tbpm: " + bpm +
				//		"\n\tEnergy: " + energy +
				//		"\n\tDanceability: " + danceability +
				//		"\n\tLoudness: " + loudness +
				//		"\n\tLiveness: " + liveness +
						"");
				*/
				fileLoaded = true;
			}

	    } catch (NullPointerException e){ // if file name is null, we throw an IOException per the instructions of the assignment
		   throw new IOException("File name is NULL");

		} catch (FileNotFoundException e){
			throw new IOException("File not found");


		/*
		   when the file is not formatted to expectations, we throw an IOException.
		   This will catch the exception and print out what the issue is. This assumes
		   an explanation is provided, which there should. See above catch blocks for
		   reference on how this is done.
		 */
		} catch (IOException e){
		   System.out.println(e);
	    }
    }

	/**
	 * The following method gets the data for a song. It
	 * handles quotation logic when returning the data:
	 *		Single quotes: Commas present between opening and closing quotations
	 *			 should NOT be considered as separating points of data, and
	 *			 instead be included in the data (string).
	 * 		Double quotes: return a single quote. Commas
	 * 			present between sets of double quotes are still considered to be
	 * 			separating points of data.
	 * @param index is the index representing where in the line of song data
	 *              the particular data we want is located (should be a string)
	 * @param songData is the line of data for a particular song
	 * @return data
	 */

    public String findStringData(int index, String songData){
	    String data = ""; // stores data that will be returned
		char[] charArray = songData.toCharArray(); // converts songData to an array of chars to find commas that seperates different points of data
		char c = ' '; // used to find the commas and quotations that deperates difference points of fata in the charArray

	    /**
	     * Makes sure any commas found between quotations does not increment commaCnt.
	     * quotes = true when an OPENING quotation is found in the data
	     * quotes = false when a) no opening quotation has been found in the data or
	     * 		b) the last quotation found in the data was a CLOSING quotation.
	     * 		In other words, if the number of quotations is odd, quotes = true.
	     * 		Else if even, quotes = false.
	     * all opening quotations are assumed to have a respective closing quotation.
	     */
		 boolean quotes = false;

	    /**
	     * var is incremented to look at the next element in the charArray before
	     * storing the value into c. It is initialized as the first index where the
	     * correct data is expected to be in the charArray from songData
	     */
	     int charArrayIndex = findDataLocation(charArray, index);


		 /**
		  * The following method gets a particular String representing a point of data
		  * for a song. It handles quotation logic when returning the data:
	     * if c is a quotation, we check to see if there is another quotation
	     * 		following it. If so, then we want to add ONE quotation to
	     * 		data as that is what "" means. We then increment
	     * 		charArrayIndex to essentially "skip" over the second one
	     * 		in the array so that it also doesn't get added.
	     * if c is NOT a quotation, then we see if quotes is set to true.
	     * 		if so, we add c to data.
	     * in the case that quotes = false, and c is a comma, then that means
	     * 		we have reached the end of the data string, so we break
	     * 		the loop.
	     */
	    while(charArrayIndex < charArray.length){
		    c = charArray[charArrayIndex];

		    if(c == '\"'){
			    if(charArray[charArrayIndex + 1] == '\"'){
		        	data += c;
				charArrayIndex++;
			    } else {
			    	quotes = !quotes;
			    }
		    } else if ((quotes) || (c != ',')){
			    data += c;
		    } else {
			    break;
		    }
		    charArrayIndex++; // increment to begin looking at the next character in the array
	    }


	    return data;
    }

	/**
	 * The following method gets a particular int representing a point of data
	 * for a song.
	 *
	 * @param index is the index representing where in the line of song data
	 *              the particular data we want is located (should be an int)
	 * @param songData is the line of data for a particular song
	 * @return data
	 */
    public int findIntData(int index, String songData){
	    String data = ""; // stores data that will be returned
            char[] charArray = songData.toCharArray(); // converts songData to an array of chars to find commas that separates different points of data
            char c = ' '; // used to find the commas and quotations that separates difference points of data in the charArray

	    /**
	     * var is incremented to look at the next element in the charArray before
	     * storing the value into c. It is initialized as the first index where the
	     * correct data is expected to be in the charArray from songData
	     */
	    int charArrayIndex = findDataLocation(charArray, index);

	    c = charArray[charArrayIndex];

	    while(c != ','){
			data += c;
		    c = charArray[++charArrayIndex];
		}

	   /** the following code converts the data to an integer that the method can return */
	    int intData;

	    try {
	    	intData = Integer.parseInt(data);
	    } catch (NumberFormatException e){
	    		    throw new NumberFormatException("Data = ("+data+") can't be converted into a number! See findIntData()");

	    }

	    return intData;
    }

    /**
     * locates where in the songData string (converted to charArray) that a particular kind of data is.
     * @param charArray is the line of data for (in the case of song.csv) a song that has been
     * 		converted to an array of characters
     * @param index is used to find the location within the charArray where commas ',' that are NOT
     * 		between quoations "" seperate the different points.
     * 		for example, if the data is expected to be at index = 3, then I would need to pass
     * 		3 of those commas before I reach my desired location in the array.
     * @return the location (the index) in the charArray that my desired data begins
     */
    public int findDataLocation(char[] charArray, int index){
	    char c = charArray[0]; // var used to store the characters in the line of data from songData, which has been converted to an array of chars (charArray)
		int commaCnt = 0; // counts the number of commas that have been past when finding the location of the data in the charArray
	    boolean quotes = false; // any commas found between quotations should not increment commaCnt and this variables makes sure this behavior happens
	    int dataLocation = 0; // tracks the location of the data within the charArray

		if(c == '\"'){
			quotes = true;
	    }

		while(index > commaCnt){
			c = charArray[++dataLocation];
			if(quotes){
				if(c == '\"'){
					quotes = false;
				}
			} else {
				if(c == ','){
					commaCnt++;
				} else if (c == '\"'){
					quotes = true;
				}
			}
		}


		if(index != 0){
			++dataLocation;
		}

	    return dataLocation;
    }

    /**
     * Retrieves a list of song titles from the tree passed to the constructor.
     * The songs should be ordered by the songs' Loudness, and that fall within
     * the specified range of Loudness values.  This Loudness range will
     * also be used by future calls to the setFilter and getmost Danceable methods.
     *
     * If a Speed filter has been set using the setFilter method
     * below, then only songs that pass that filter should be included in the
     * list of titles returned by this method.
     *
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a null
     * high argument means that there is no maximum Loudness to include in
     * the returned list.
	 *
	 * See filterSongs() for more information.
     *
     * @param low is the minimum Loudness of songs in the returned list
     * @param high is the maximum Loudness of songs in the returned list
     * @return List of titles for all songs from low to high, or an empty
     *     list when no such songs have been loaded
     */

    public List<String> getRange(Integer low, Integer high){
		rangeSet = !((high == null) && (low == null)); // set rangeSet variable to the appropriate value
		rangeLow = low; // set rangeLow
		rangeHigh = high; // set rangeHigh

		if(low != null && high != null){
			if(rangeLow > rangeHigh) {
				throw new IllegalArgumentException("Passed minimum loudness is greater than the passed maximum loudness");
		}	
		}

		ArrayList<Song> songs = filterSongs(); // stores the songs that are within the range and a loudness
		ArrayList<String> titles = new ArrayList<>(); // stores the titles of the songs in ArrayList songs by loudness


		songs.sort(new sortSongsByLoudnessComparator());

		for (int i = 0; i < songs.size(); i++) {
			titles.add(songs.get(i).getTitle());
		}

		return titles;
    }


    /**
     * Retrieves a list of song titles that have a Speed that is
     * larger than the specified threshold.  Similar to the getRange
     * method: this list of song titles should be ordered by the songs'
     * Loudness, and should only include songs that fall within the specified
     * range of Loudness values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high Loudness bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and getMostDanceable methods.
     *
     * When null is passed as the threshold to this method, then no Speed
     * threshold should be used.  This effectively clears the filter.
	 *
	 * See filterSongs() for more information.
     *
     * @param threshold filters returned song titles to only include songs that
     *     have a Speed that is larger than this threshold.
     * @return List of titles for songs that meet this filter requirement, or
     *     an empty list when no such songs have been loaded
     */

    public List<String> setFilter(Integer threshold){
		speedThreshold = threshold; // set speedThreshold variable to threshold

		ArrayList<Song> songs = filterSongs();
		ArrayList<String> titles = new ArrayList<>();

		// sort the songs by loudness and add their titles to titles ArrayList
		songs.sort(new sortSongsByLoudnessComparator());
			for(int i = 0; i < songs.size(); i++){
			titles.add(songs.get(i).getTitle());
		}

		/* // This is a debug statement
		System.out.println("\nAfter applying filter: ");
			for(int i = 0; i < songs.size(); i++){
		  System.out.println("Title: " + songs.get(i).getTitle() +
				 "\n\tSoundness: " + songs.get(i).getLoudness() +
				 "\n\tSpeed(BPM): " + songs.get(i).getBPM());
		}
		System.out.println("----------------------------------------");
		*/

		return titles;
    }

	/**
	 * Filters songs by loudness range and speed threshold
	 * @return the list of songs
	 */
	public ArrayList<Song> filterSongs() {
		ArrayList<Song> songs = new ArrayList<>();
		Song song; // temporarily stores a song's information
		Iterator<Song> iterator = backend_tree.iterator(); // iterator for the backend tree

		if(fileLoaded) {
			/**
			 * filter songs into a list that conforms to the range and filter
			 * if it was previously set.
			 */
			// if a range and speedThreshold was set, then add songs to the song list that lie in the loudness range and speed threshold
			if (rangeSet && speedThreshold != null) {
				while (iterator.hasNext()) { // for every song in the tree
					song = iterator.next();
					if (song.getBPM() > speedThreshold) { // only consider songs that pass the speed filter
						/** adds the song to the songs list if its loudness falls within the right range */
						if (rangeLow != null && rangeHigh != null) { // if a low range AND a high range was set
							if ((song.getLoudness() >= rangeLow) && (song.getLoudness() <= rangeHigh)) {
								songs.add(song);
							}
						} else if ((rangeLow == null) && (song.getLoudness() <= rangeHigh)) { // if only the high range was set
							songs.add(song);
						} else if (song.getLoudness() >= rangeLow) { // if only the low range was set
							songs.add(song);
						}
					}
				}
			} else if (rangeSet) { // if ONLY a range was set
				while (iterator.hasNext()) { // for every song in the tree
					song = iterator.next();
					/** adds the song to the songs list if its loudness falls within the right range */
					if (rangeLow != null && rangeHigh != null) {
						if ((song.getLoudness() >= rangeLow) && (song.getLoudness() <= rangeHigh)) {
							songs.add(song);
						}
					} else if (rangeLow == null && (song.getLoudness() <= rangeHigh)) {
						songs.add(song);
					} else if ((song.getLoudness() >= rangeLow)) {
						songs.add(song);
					}
				}
			} else if (speedThreshold != null) { // if ONLY a speedThreshold was set
				while (iterator.hasNext()) { // for every song in the tree
					song = iterator.next();
					if (song.getBPM() > speedThreshold) { // add songs that pass the speed filter
						songs.add(song);
					}
				}
			} else { // no range or speed filter set, so we add ALL songs from the tree to our list
				while (iterator.hasNext()) { // for every song in the tree
					songs.add(iterator.next());
				}
			}
		} else {
			System.out.println("\n====== You must load a file before any songs can be printed ======\n");
		}

		return songs;
	}


    /**
     * This method returns a list of song titles representing the five
     * most Danceable songs that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to setFilter.  The order of the song titles in this
     * returned list is up to you.
     *
     * If fewer than five such songs exist, return all of them.  And return an
     * empty list when there are no such songs.
     *
     * @return List of five most Danceable song titles
     */

    public List<String> fiveMost() {
		ArrayList<Song> songs = filterSongs();
		ArrayList<String> titles = new ArrayList<>();

		/* ----- get the top five most danceable songs from the remaining titles ----- */
		songs.sort(new sortSongsByDanceabilityComparator()); // sort songs list by danceability

		/** debug */ //System.out.println("\n\nTOP FIVE MOST DANCEABLE SONGS:");

		// add top 5 from the newly sorted list
		if(fileLoaded) {
			for (int i = 0; (i < songs.size()) && (i < 5); i++) {
				titles.add(songs.get(i).getTitle());
				/** debug */ //System.out.println("#" + (i+1) + ": " + songs.get(i).getTitle() + " (" + songs.get(i).getDanceability() + ")");
			}
		}


		return titles;
    }
}

// Used to sort a list of songs from least to most loud in getRange() and setFilter()
class sortSongsByLoudnessComparator implements Comparator<Song> {
	@Override
	public int compare(Song song1, Song song2) {
		return Integer.compare(song1.getLoudness(), song2.getLoudness());
	}
}

// Used to sort a list of songs from most danceable to least danceable in fiveMost()
class sortSongsByDanceabilityComparator implements Comparator<Song> {
	@Override
	public int compare(Song song1, Song song2) {
		return Integer.compare(song2.getDanceability(), song1.getDanceability());
	}
}



















































































































































































































































































































































































































































































































































































































































