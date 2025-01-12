package iSongly;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {
    private BackendInterface backend;
    private Scanner scanner;

    /*
     * Create a new instance of the Frontend class
     * @param scanner the Scanner object used to read user input
     * @param backend the BackendInterface object used to communicate with the backend
     */
    public Frontend(Scanner scanner, BackendInterface backend) {
        this.scanner = scanner;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() { //repeatedly gives the user an opportunity to issue new commands until they select Q to quit
        boolean continue_loop = true;
        while (continue_loop) {
            displayMainMenu();
            String command = scanner.nextLine().toUpperCase();

            //check each user input for a matching command - or let them redo if invalid
            switch (command) {
                case "L":
                    loadFile();
                    break;
                case "G":
                    getSongs();
                    break;
                case "F":
                    setFilter();
                    break;
                case "D":
                    displayTopFive();
                    break;
                case "Q":
                    continue_loop = false;
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }

    @Override
    public void displayMainMenu() {
        System.out.println("Welcome to iSongly!");
        System.out.println("Please select an option:");
        System.out.println("[L]oad Song File");
        System.out.println("[G]et Songs by Loudness");
        System.out.println("[F]ilter Songs by Speed");
        System.out.println("[D]isplay five most Danceable");
        System.out.println("[Q]uit");
    }

    @Override
    public void loadFile() {

        //get file input and send to backend for processing - if not loaded successfully, try again
        while (true) {
            System.out.print("Enter the filename to load: ");
            String filename = scanner.nextLine();
            try {
                backend.readData(filename);
                System.out.println("Data loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading data: " + e.getMessage());
                continue;
            }
            break;
        }
    }

    @Override
    public void getSongs() {
        //ask question repeatably until user enters a valid number
        int min = 0;
        int max = 100;
        
        //ask for min number
        while(true){
            try{
                System.out.print("Enter the minimum Loudness: ");
                min = Integer.parseInt(scanner.nextLine());
            } catch(Exception e){
                System.out.println("Invalid input, try again");
                continue;
            }
            break;
        }

        //ask for max num
        while(true){ //check if min is less than max
            try{
                System.out.print("Enter the maximum Loudness: ");
                max = Integer.parseInt(scanner.nextLine());
                if(min > max) {
                    System.out.println("Please enter a number greater than the min.");
                    continue;
                }
            } catch(Exception e){
                System.out.println("Invalid input, try again");
                continue;
            }
            break;
        }
        
        //print songs out with dash
        List<String> titles = backend.getRange(min, max);
        System.out.println("Songs by Loudness:");
        for (String title : titles) {
            System.out.println("- " + title);
        }
    }

    /*
     * Sets a filter for song speed (BPM). Only songs with a BPM greater than the specified threshold
     * will be included in future results.
     * 
     * When null is passed as the threshold to this method, then no Speed
     * threshold should be used.  This effectively clears the filter.
     * 
     * @param threshold the BPM threshold to filter songs by, or null to clear the filter
     * @return a list of song titles that meet the speed filter requirement
     * or an empty list when no such songs have been loaded
     */
    @Override
    public void setFilter() { //ask for input
        //ask for input
        Integer threshold = 0;
        while(true){ //check if min is less than max
            try{
                System.out.print("Enter the minimum Speed threshold: ");
                threshold = Integer.parseInt(scanner.nextLine());
                if(threshold < 0) {
                    System.out.println("Please enter a non-negative number.");
                    continue;
                }
                System.out.println("Filter set successfully.");
            } catch(Exception e){
                System.out.println("Invalid input, try again");
                continue;
            }
            break;
        }
        backend.setFilter(threshold);
        System.out.println("Filter set successfully.");
    }

    @Override
    public void displayTopFive() { //print titles from backend
        //print titles from backend
        List<String> titles = backend.fiveMost();
        if (titles.isEmpty()) {
            System.out.println("No songs match the current Loudness range and Speed filter.");
        } else {
            System.out.println("The five most Danceable songs are:");
            for (String title : titles) {
                System.out.println("- " + title);
            }
        }
    }
}
