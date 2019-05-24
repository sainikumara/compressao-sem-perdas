package compressao;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IO {

    /**
     *
     * @param filename
     * @return
     */
    public byte[] readBytesFromFile(String filename) {
        String userDir = System.getProperty("user.dir");

        byte[] dataAsBytes = null;

        try {
            dataAsBytes = Files.readAllBytes(Paths.get(userDir, filename));
        } catch (IOException e) {
            System.out.println("caught exception: " + e.getMessage());
        }

        return dataAsBytes;
    }

    /**
     *
     * @param filename
     * @return
     */
    public String stringFromFile(String filename) {
        String dataAsString = new String(readBytesFromFile(filename));
        return dataAsString;
    }

    /**
     *
     * @param filename
     * @param dataToWrite
     */
    public void writeDictionaryToFile(String filename, List<LZ77DictionaryEntry> dataToWrite) {
        byte[] dataAsBytes = this.convertDictionaryToByteArray(dataToWrite);

        String userDir = System.getProperty("user.dir");
        try {
            Files.write(Paths.get(userDir, filename), dataAsBytes);
        } catch (IOException e) {
            System.out.println("caught exception: " + e.getMessage());
        }
    }

    /**
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

    /**
     *
     * @param dataToConvert
     * @return
     */
    public byte[] convertDictionaryToByteArray(List<LZ77DictionaryEntry> dataToConvert) {
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
     *
     * @param filename
     * @return
     */
    public List<LZ77DictionaryEntry> compressedDataFromFile(String filename) {
        byte[] dataAsBytes = this.readBytesFromFile(filename);
        List<LZ77DictionaryEntry> dictionary = new ArrayList<>();

        for (int i = 0; i < dataAsBytes.length; i += 3) {
            int offsetToBeginningOfMatch = dataAsBytes[i];
            int matchingStringLength = dataAsBytes[i + 1];
            char charFollowingMatch = (char) dataAsBytes[i + 2];
            dictionary.add(
                    new LZ77DictionaryEntry(
                            offsetToBeginningOfMatch,
                            matchingStringLength,
                            charFollowingMatch
                    )
            );
        }

        return dictionary;
    }
}
