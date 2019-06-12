package compressao;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IO {

    /**
     * Read data from file, return as a byte array.
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
     * @param filename
     * @return the data as a string
     */
    public String stringFromFile(String filename) {
        String dataAsString = new String(readBytesFromFile(filename));
        return dataAsString;
    }

    /**
     * Write compressed data to file. Convert it first into a byte array and write as such.
     * @param filename
     * @param dataToWrite
     */
    public void writeCompressedStringToFile(String filename, List<LZ77CompressedEntry> dataToWrite) {
        byte[] dataAsBytes = this.convertListToByteArray(dataToWrite);

        this.writeBytesToFile(filename, dataAsBytes);
    }

    /**
     * Convert list of LZ77CompressedEntries into a data array for writing into file.
     * Each field in each entry takes up one byte in the array.
     * @param dataToConvert the list to be converted
     * @return data from the list as a byte array
     */
    public byte[] convertListToByteArray(List<LZ77CompressedEntry> dataToConvert) {
        byte[] dataAsBytes = new byte[3 * dataToConvert.size()];

        int j = 0;

        for (int i = 0; i < dataToConvert.size(); i++) {
            dataAsBytes[j++] = (byte) dataToConvert.get(i).getOffset();
            dataAsBytes[j++] = (byte) dataToConvert.get(i).getmatchLength();
            dataAsBytes[j++] = (byte) dataToConvert.get(i).getFollowingChar();
        }

        return dataAsBytes;
    }

    /**
     * Write any data in byte array form into a file
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

    /**
     * Read string based compressed data that has been written to a file
     * @param filename
     * @return compressed data (as a list of LZ77CompressedEntries)
     */
    public List<LZ77CompressedEntry> compressedStringBasedDataFromFile(String filename) {
        byte[] dataAsBytes = this.readBytesFromFile(filename);
        List<LZ77CompressedEntry> compressed = new ArrayList<>();

        for (int i = 0; i < dataAsBytes.length; i += 3) {
            int offsetToBeginningOfMatch = dataAsBytes[i];
            int matchingStringLength = dataAsBytes[i + 1];
            char charFollowingMatch = (char) dataAsBytes[i + 2];
            compressed.add(
                    new LZ77CompressedEntry(
                            offsetToBeginningOfMatch,
                            matchingStringLength,
                            charFollowingMatch
                    )
            );
        }

        return compressed;
    }
}
