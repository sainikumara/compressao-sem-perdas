package compressao;

import util.ArrayCopier;
import java.io.IOException;

public class LZ77Decompress {

    ArrayCopier arrayCopier = new ArrayCopier();
    byte[] reconstructedData;
    int reconstructedDataPointer;
    int compressedDataPointer;

    /**
     * LZ77 decompression implementation for byte array form data
     *
     * @param compressedData: the compressed form of the original byte array
     * @return byte array that has been reconstructed from compressed data
     * @throws java.io.IOException
     */
    public byte[] decompressData(byte[] compressedData) throws IOException, NullPointerException {
        reconstructedData = new byte[10 * compressedData.length];
        reconstructedDataPointer = 0;
        compressedDataPointer = 0;

        while (compressedDataPointer < compressedData.length) {
            reconstructOneSequence(compressedData);
        }
        reconstructedData = arrayCopier.copyOfRange(reconstructedData, 0, reconstructedDataPointer);
        return reconstructedData;
    }

    /**
     * Reconstruct one sequence for the decompressed data based on compressed
     * data
     *
     * @param compressedData
     */
    private void reconstructOneSequence(byte[] compressedData) {
        int matchLength = (int) compressedData[compressedDataPointer++];
        int offsetToBeginningOfMatch = 0;
        byte followingByte = 0;
        if (matchLength == 0) {
            followingByte = compressedData[compressedDataPointer++];
        } else {
            offsetToBeginningOfMatch = (int) compressedData[compressedDataPointer++];
            followingByte = compressedData[compressedDataPointer++];
        }
        addReconstructedSequence(matchLength, offsetToBeginningOfMatch, followingByte);
    }

    /**
     * Reconstruct a sequence of data based on one set of values from the
     * compressed data
     *
     * @param offsetToBeginningOfMatch
     * @param matchLength
     * @param followingByte
     */
    public void addReconstructedSequence(int matchLength, int offsetToBeginningOfMatch, byte followingByte) {
        if (matchLength > 0) {
            for (int j = 0; j < matchLength; j++) {
                reconstructedData[reconstructedDataPointer++] = reconstructedData[reconstructedDataPointer - 1 - offsetToBeginningOfMatch];
            }
            reconstructedData[reconstructedDataPointer++] = followingByte;
        } else {
            reconstructedData[reconstructedDataPointer++] = followingByte;
        }
    }
}
