package iSongly;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 * This is a placeholder for the fully working Backend that will be developed
 * by one of your teammates this week and then integrated with your role code
 * in a future week.  It is designed to help develop and test the functionality
 * of your own Frontend role code this week.  Note the limitations described
 * below. Fix with these changes: Update: 9/20: several errors have been reported
 * with the provided Backend_Placeholder class, please fix the following in your 
 * Backend_Placeholder: 1) the first line: public interface Backend_Placeholder 
 * should be changed to: public class Backend_Placeholder implements BackendInterface 
 * and 2) the name of the final method should be updated to fiveMost, so that it 
 * matches what is correctly defined in the BackendInterface.java, 3) the body of 
 * readData should include a call to tree.insert rather than p.insert, 4) there 
 * is a missing semicolon at the end of the line: titles.add(song.getTitle()); 
 * and 5) this file also needs an import statement for ArrayList to compile. 
 */
public class Backend_Placeholder implements BackendInterface {

    IterableSortedCollection<Song> tree;    

    public Backend_Placeholder(IterableSortedCollection<Song> tree) {
        this.tree = tree;
    }

    public void readData(String filename) throws IOException {
        tree.insert(new Song("DJ Got Us Fallin' In Love (feat. Pitbull)",
                             "Usher", "atl hip hop", 2010, 120, 86, 66, -3, 8));
    }

    public List<String> getRange(Integer low, Integer high) {
        String lowString = (char) ('A' + low) + " string";
        Song lowSong = new Song(lowString, lowString, lowString,
                                 low, low, low, low, low, low);
        String highString = (char) ('A' + high) + " string";
        Song highSong = new Song(highString, highString, highString,
                                 high, high, high, high, high, high);
        tree.setIteratorMin(lowSong);
        tree.setIteratorMax(highSong);
        return fiveMost();
    }

    public List<String> setFilter(Integer threshold) {
        return fiveMost();
    }

    public List<String> fiveMost() {
        List<String> titles = new ArrayList<>();
        for (Song song : tree) {
            titles.add(song.getTitle());
        }
        return titles;
    }
}
