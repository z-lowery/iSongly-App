import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 * This is a placeholder for the fully working Backend It is 
 * designed to help develop and test the functionality
 * of the Frontend role code before the final backend implmentation.  
 * Note the limitations.
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
