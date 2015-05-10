package com.mygdx.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by William Schiela on 4/25/2015.
 * a class to handle opening, writing to, and closing the .csv file
 * the data file is saved in the android/assets folder and has the following format:

 * Date and Time:, <current timestamp>
 * Player, <player name given at login>

 * Optimal Path, Actual Path
 * <optimal value 1>, <actual value 1>
 * <optimal value 2>, <actual value 2>
 * ...etc
 */
public class DataFile {
    BufferedWriter bw;

    public DataFile(String filename) {
        try {
            bw = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            System.out.println("Error opening research data file for writing.");
        }
    }

    // writes the optimal position and the actual position to the .csv file
    public void write(float optimal, float actual) {
        try {
            String op = Float.toString(optimal);
            String act = Float.toString(actual);

            bw.append(op);
            bw.append(',');
            bw.append(act);
            bw.append('\n');
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error writing to data file.");
        }
    }

    // writes the username and timestamp to the top of data file at beginning of each run
    public void writeHeader(String username, String timestamp) {
        try {
            bw.append("Date and Time:" + "," + timestamp);
            bw.append('\n');
            bw.append("Player:" + "," + username);
            bw.append("\n \n");
            bw.append("Optimal Path" + "," + "Actual Path");
            bw.append('\n');
        } catch (IOException e) {
            System.out.println("Error writing header to data file.");
        }
    }

    // closes the .csv file upon user "quit"
    public void close() {
        try {
            bw.close();
        } catch (IOException e) {
            System.out.println("Error closing the data file.");
        }
    }
}
