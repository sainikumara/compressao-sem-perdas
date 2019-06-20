package compressao;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IO {

    /**
     * Read data from file, return as a byte array.
     *
     * @param filename
     * @return data as a byte array
     */
    public byte[] readBytesFromFile(String filename) {
        String userDir = System.getProperty("user.dir");

        byte[] dataAsBytes = null;

        try {
            dataAsBytes = Files.readAllBytes(Paths.get(userDir, filename));
        } catch (IOException e) {
            System.out.println("Seems like the file does not exist: " + e.getMessage());
        }
        return dataAsBytes;
    }

    /**
     * Read a string from a file using readBytesFromFile
     *
     * @param filename
     * @return the data as a string
     */
    public String stringFromFile(String filename) {
        String dataAsString = new String(readBytesFromFile(filename));
        return dataAsString;
    }

    /**
     * Write any data in byte array form into a file
     *
     * @param filename
     * @param dataToWrite
     */
    public void writeBytesToFile(String filename, byte[] dataToWrite) {

        String userDir = System.getProperty("user.dir");
        try {
            Files.write(Paths.get(userDir, filename), dataToWrite);
        } catch (IOException e) {
            System.out.println("caught exception: " + e.getMessage());
        }
    }

    /**
     * Write text to file. This method exists for testing purposes.
     *
     * @param filename
     * @param textToWrite
     */
    public void writeTextToFile(String filename, String textToWrite) {
        String userDir = System.getProperty("user.dir");
        Charset charset = Charset.forName("US-ASCII");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(userDir, filename), charset)) {
            writer.write(textToWrite, 0, textToWrite.length());
        } catch (IOException e) {
            System.out.println("caught exception: " + e.getMessage());
        }
    }
}
