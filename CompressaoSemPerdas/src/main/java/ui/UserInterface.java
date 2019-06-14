package ui;

import compressao.CompressionInfo;
import compressao.IO;
import compressao.LZ77Compress;
import compressao.LZ77Decompress;
import java.util.Scanner;

public class UserInterface {

    private static Scanner scanner;
    IO io;
    LZ77Compress compressor;
    LZ77Decompress decompressor;
    CompressionInfo info;

    public UserInterface() {
        scanner = new Scanner(System.in);
        io = new IO();
        compressor = new LZ77Compress();
        decompressor = new LZ77Decompress();
        info = new CompressionInfo();
    }

    /**
     * Starts the command-line user interface, prompts for input from the user
     */
    public void start() {

        System.out.println("If you want to compress a file, write c and the path to the file.");
        System.out.println("Example: c /resources/large_text_file.txt");
        System.out.println("If you want to decompress a file, write d and the path to the file.");
        System.out.println("Example: d /resources/large_text_file_compressed");
        System.out.println("\nWrite q if you want to quit.");

        while (true) {
            System.out.print("\nWrite your command: ");
            String userInput = scanner.nextLine();
            if (userInput.startsWith("q")) {
                break;
            }
            if (userInput.startsWith("c ") && userInput.length() > 2) {
                compress(userInput.substring(2, userInput.length()));
            } else if (userInput.startsWith("d ") && userInput.length() > 2) {
                decompress(userInput.substring(2, userInput.length()));
            } else {
                System.out.println("\nNot a valid input");
            }
        }
    }

    /**
     * Uses LZ77Compress to compress the data in the file given by the user.
     * Writes the compressed data into a file with "_compressed" appended to the
     * original file name.
     *
     * @param filename
     */
    private void compress(String filename) {
        int searchWindowLength = 127;
        int lookAheadWindowLength = 7;

        byte[] bytesToCompress = io.readBytesFromFile(filename);
        if (bytesToCompress == null) {
            System.out.println("No readable file.");
            return;
        }

        byte[] compressedData = compressor.compressBytes(bytesToCompress, searchWindowLength, lookAheadWindowLength);

        String compressedFilename = formatFilename(filename, ".", "_compressed");
        io.writeBytesToFile(compressedFilename, compressedData);

        System.out.println("");
        System.out.println("The compressed data from " + filename + " was written into: " + compressedFilename);

        info.LZ77Info(filename, compressedFilename);
    }

    /**
     * Uses LZ77Decompress to decompress the data in the file given by the user.
     * Writes the decompressed data into a file with "_decompressed" appended to
     * the original file name.
     *
     * @param filename
     */
    private void decompress(String filename) {
        byte[] bytesFromFile = io.readBytesFromFile(filename);
        byte[] decompressedBytes;

        try {
            decompressedBytes = decompressor.decompressBytes(bytesFromFile);
            String decompressedFilename = formatFilename(filename, "_compressed", "_decompressed");

            io.writeBytesToFile(decompressedFilename, decompressedBytes);

            System.out.println("");
            System.out.println("The decompressed data from " + filename + " was written into: " + decompressedFilename);
        } catch (Exception e) {
            System.out.println("Can't complete decompression.\n"
                    + "The file " + filename + " may not be in proper format.");
        }
    }

    /**
     * Formats a new filename for the compressed or decompressed file, based on
     * the original filename
     *
     * @param filename
     * @param partToRemove
     * @param partToAdd
     * @return new filename based on the original
     */
    private String formatFilename(String filename, String partToRemove, String partToAdd) {
        String newFilename;
        if (filename.contains(partToRemove)) {
            newFilename = filename.substring(0, filename.indexOf(partToRemove)) + partToAdd;
        } else {
            newFilename = filename + partToAdd;
        }

        return newFilename;
    }
}
