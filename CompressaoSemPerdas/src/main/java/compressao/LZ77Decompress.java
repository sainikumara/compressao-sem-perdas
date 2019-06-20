package compressao;

import java.io.IOException;

public class LZ77Decompress {

    ArrayCopier arrayCopier = new ArrayCopier();
    byte[] reconstructedData;

    /**
     * LZ77 decompression implementation for byte array form data
     *
     * @param compressedData: the compressed form of the original byte array
     * @return byte array that has been reconstructed from compressed data
     * @throws java.io.IOException
     */
    public byte[] decompressData(byte[] compressedData) throws IOException {
        reconstructedData = new byte[10 * compressedData.length];
        int reconstructedDataPointer = 0;

        for (int i = 0; i < compressedData.length; i += 3) {
            reconstructedDataPointer = reconstructOneSequence(reconstructedDataPointer, (int) compressedData[i], (int) compressedData[i + 1], compressedData[i + 2]);
        }
        reconstructedData = arrayCopier.copyOfRange(reconstructedData, 0, reconstructedDataPointer);
        return reconstructedData;
    }

    /**
     * Reconstruct a sequence of data based on one set of values from the
     * compressed data
     *
     * @param reconstructedDataPointer
     * @param offsetToBeginningOfMatch
     * @param matchLength
     * @param followingByte
     * @return new value for reconstructedDataPointer
     */
    public int reconstructOneSequence(int reconstructedDataPointer, int offsetToBeginningOfMatch, int matchLength, byte followingByte) {
        if (matchLength > 0) {
            for (int j = 0; j < matchLength; j++) {
                reconstructedData[reconstructedDataPointer++] = reconstructedData[reconstructedDataPointer - 1 - offsetToBeginningOfMatch];
            }
        }
        reconstructedData[reconstructedDataPointer++] = followingByte;
        return reconstructedDataPointer;
    }
}
