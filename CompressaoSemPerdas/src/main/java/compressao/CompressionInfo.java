package compressao;

import java.util.ArrayList;

public class CompressionInfo {
    /**
     * Shows information on how successful the compression was: the size of the
     * resulting dictionary and the compression ratio
     * 
     * @param stringToCompress: original string
     * @param dictionary: compressed form of the string
     * @param searchWindowLength
     * @param lookAheadWindowLength 
     */
    public void LZ77Info(String stringToCompress,
            ArrayList<LZ77DictionaryEntry> dictionary,
            int searchWindowLength,
            int lookAheadWindowLength) {
        int bitsPerEntry = Integer.bitCount(searchWindowLength) 
                + Integer.bitCount(lookAheadWindowLength) + 8;
        int dictionarySizeInBits = bitsPerEntry * dictionary.size();
        int stringSizeInBits = 8 * stringToCompress.length();
        double relativeCompression = (double) dictionarySizeInBits / stringSizeInBits;

        System.out.println("dictionary size: " + dictionary.size());
        System.out.println("Relative compression: " + relativeCompression);
    }
}
