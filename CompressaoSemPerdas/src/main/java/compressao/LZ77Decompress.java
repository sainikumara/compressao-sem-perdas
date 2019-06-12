package compressao;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LZ77Decompress {

    /**
     * NOT IN USE!
     * LZ77 decompression implementation for String form data
     *
     * @param compressed: the compressed form of the original string
     * @return the reconstructed string
     */
    public String decompressString(List<LZ77CompressedEntry> compressed) {
        StringBuffer reconstructString = new StringBuffer();

        for (LZ77CompressedEntry entry : compressed) {
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
     * @param compressed: the compressed form of the original byte array
     * @return byte array that has been reconstructed from compressed data
     * @throws java.io.IOException
     */
    public byte[] decompressBytes(byte[] compressed) throws IOException {
        byte[] reconstructBytes = new byte[10 * compressed.length];
        int bytePointer = 0;

        for (int i = 0; i < compressed.length; i+=3) {
            int offsetToBeginningOfMatch = (int) compressed[i];
            int matchingBytesLength = (int) compressed[i+1];
            byte byteFollowingMatch = compressed[i+2];

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
