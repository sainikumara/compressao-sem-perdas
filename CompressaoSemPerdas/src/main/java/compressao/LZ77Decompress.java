package compressao;

import java.io.IOException;

public class LZ77Decompress {
    ArrayCopier arrayCopier = new ArrayCopier();

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

        reconstructBytes = arrayCopier.copyOfRange(reconstructBytes, 0, bytePointer);
        return reconstructBytes;
    }
}
