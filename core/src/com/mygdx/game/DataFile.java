package com.mygdx.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by William Schiela on 4/25/2015.
 */
// a class to handle opening, writing to, and closing the .csv file
// the data file is saved in the android/assets folder
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
            System.out.println("Writing to file");
            String op = Float.toString(optimal);
            String act = Float.toString(actual);

            System.out.println(optimal + "," + actual);
            bw.append(op);
            bw.append(',');
            bw.append(act);
            bw.append('\n');
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error writing to data file.");
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
