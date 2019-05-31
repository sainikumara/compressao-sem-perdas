package compressao;

import java.util.Arrays;
import java.util.List;

public class LZ77Decompress {

    /**
     * LZ77 decompression implementation for String form data
     *
     * @param dictionary: the compressed form of the original string
     * @return the reconstructed string
     */
    public String decompressString(List<LZ77DictionaryEntry> dictionary) {
        StringBuffer reconstructString = new StringBuffer();

        for (LZ77DictionaryEntry entry : dictionary) {
            if (entry.matchingStringLength == 0) {
                reconstructString.append(entry.charFollowingMatch);
            } else {
                for (int i = 0; i < entry.matchingStringLength; i++) {
                    char charToAdd = reconstructString.charAt(reconstructString.length() - entry.offsetToBeginningOfMatch);
                    reconstructString.append(charToAdd);
                }
                reconstructString.append(entry.charFollowingMatch);
            }
        }

        return new String(reconstructString);
    }

    /**
     * LZ77 decompression implementation for byte array form data
     * 
     * @param dictionary: the compressed form of the original byte array
     * @return byte array that has been reconstructed from compressed dictionary
     */
    public byte[] decompressBytes(byte[] dictionary) {
        byte[] reconstructBytes = new byte[3 * dictionary.length];
        int bytePointer = 0;

        for (int i = 0; i < dictionary.length; i+=3) {
            int offsetToBeginningOfMatch = (int) dictionary[i];
            int matchingBytesLength = (int) dictionary[i+1];
            byte byteFollowingMatch = dictionary[i+2];

            if (matchingBytesLength > 0) {
                for (int j = 0; j < matchingBytesLength; j++) {
                    reconstructBytes[bytePointer++] = reconstructBytes[bytePointer - 1 - offsetToBeginningOfMatch];
                }
            } 
            reconstructBytes[bytePointer++] = byteFollowingMatch;
        }

        reconstructBytes = Arrays.copyOfRange(reconstructBytes, 0, bytePointer);
        return reconstructBytes;
    }
}
