package compressao;

import java.util.ArrayList;

public class CompressionInfo {
    /**
     * Shows information on how successful the compression was: the size of the
     * resulting compressed data and the compression ratio
     * 
     * @param stringToCompress: original string
     * @param compressed: compressed form of the string
     * @param searchWindowLength
     * @param lookAheadWindowLength 
     */
    public void LZ77Info(String stringToCompress,
            ArrayList<LZ77CompressedEntry> compressed,
            int searchWindowLength,
            int lookAheadWindowLength) {
        int bitsPerEntry = Integer.bitCount(searchWindowLength) 
                + Integer.bitCount(lookAheadWindowLength) + 8;
        int compressedDataSizeInBits = bitsPerEntry * compressed.size();
        int stringSizeInBits = 8 * stringToCompress.length();
        double relativeCompression = (double) compressedDataSizeInBits / stringSizeInBits;

        System.out.println("Compressed data size: " + compressed.size());
        System.out.println("Relative compression: " + relativeCompression);
    }
}
