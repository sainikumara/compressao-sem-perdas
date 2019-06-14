package compressao;

public class LZ77Compress {
    ArrayCopier arrayCopier = new ArrayCopier();

    /**
     * LZ77 compression implementation for byte array
     *
     * @param bytesToCompress: original byte array
     * @param searchWindowLength
     * @param lookAheadWindowLength
     * @return compressed: compressed form as byte array
     */
    public byte[] compressBytes(byte[] bytesToCompress, int searchWindowLength, int lookAheadWindowLength) {
        byte[] compressed = new byte[3 * bytesToCompress.length];
        int pointerForCompressedData = 0;
        byte[] searchSubArray;
        int bytePointer = 0;
        int searchWindowStart;

        while (bytePointer < bytesToCompress.length) {
            int offsetToBeginningOfMatch = 0;
            int matchingBytesLength = 0;
            searchWindowStart = this.setSearchWindowStart(bytePointer, searchWindowLength);
            searchSubArray = this.setSearchSubArray(bytesToCompress, searchWindowStart, bytePointer);
            byte[] searchTarget = arrayCopier.copyOfRange(bytesToCompress, bytePointer, bytePointer + 1);

            if (searchSubArrayContainsSearchTarget(searchSubArray, searchTarget)) {
                while (matchingBytesLength <= lookAheadWindowLength) {
                    searchTarget = arrayCopier.copyOfRange(bytesToCompress, bytePointer, bytePointer + matchingBytesLength + 1);

                    if (searchSubArrayContainsSearchTarget(searchSubArray, searchTarget) && ((bytePointer + matchingBytesLength + 1) < bytesToCompress.length)) {
                        matchingBytesLength++;
                    } else {
                        break;
                    }
                }

                int matchPosition = searchSubArrayIndexOfSearchTarget(searchSubArray, arrayCopier.copyOfRange(bytesToCompress, bytePointer, bytePointer + matchingBytesLength));
                bytePointer += matchingBytesLength;
                offsetToBeginningOfMatch = setOffsetToBeginningOfMatch(bytePointer, searchWindowLength, matchingBytesLength, matchPosition);
            }

            compressed[pointerForCompressedData++] = (byte) offsetToBeginningOfMatch;
            compressed[pointerForCompressedData++] = (byte) matchingBytesLength;
            compressed[pointerForCompressedData++] = bytesToCompress[bytePointer];

            bytePointer++;
        }

        compressed = arrayCopier.copyOfRange(compressed, 0, pointerForCompressedData);

        return compressed;
    }

    /**
     * Set the starting index for the search window
     * 
     * @param bytePointer
     * @param searchWindowLength
     * @return the new value for searchWindowStart as integer
     */
    private int setSearchWindowStart(int bytePointer, int searchWindowLength) {
        if (bytePointer - searchWindowLength >= 0) {
            return bytePointer - searchWindowLength;
        }
        return 0;
    }

    /**
     * Set the sub array in which to search
     * 
     * @param bytesToCompress
     * @param searchWindowStart
     * @param bytePointer
     * @return the new value for the searchSubArray as a byte array
     */
    private byte[] setSearchSubArray(byte[] bytesToCompress, int searchWindowStart, int bytePointer) {
        if (bytePointer == 0) {
            return new byte[0];
        }
        return arrayCopier.copyOfRange(bytesToCompress, searchWindowStart, bytePointer);
    }

    /**
     * Find and set the offset to the beginning of match
     * 
     * @param bytePointer
     * @param searchWindowLength
     * @param matchingBytesLength
     * @param matchPosition
     * @return the new value for offsetToBeginningOfMatch as integer
     */
    private int setOffsetToBeginningOfMatch(int bytePointer, int searchWindowLength, int matchingBytesLength, int matchPosition) {
        if (bytePointer < (searchWindowLength + matchingBytesLength)) {
            return bytePointer - matchPosition - matchingBytesLength;
        } else {
            return searchWindowLength - matchPosition;
        }
    }

    /**
     * Check if the sub array contains the search target
     *
     * @param searchSubArray
     * @param searchTarget
     * @return true if the sub array contains the search target, false otherwise
     */
    public boolean searchSubArrayContainsSearchTarget(byte[] searchSubArray, byte[] searchTarget) {
        int targetPointer = 0;
        int searchWindowPointer = 0;
        boolean startedFindingTarget = false;

        while (targetPointer < searchTarget.length && searchWindowPointer < searchSubArray.length) {
            if (searchTarget[targetPointer] == searchSubArray[searchWindowPointer]) {
                targetPointer++;
                startedFindingTarget = true;
            } else if (startedFindingTarget) {
                targetPointer = 0;
                startedFindingTarget = false;
            }
            searchWindowPointer++;
        }
        return targetPointer == searchTarget.length;
    }

    /**
     * Find the first index of the search target within the sub array
     *
     * @param searchSubArray
     * @param searchTarget
     * @return the index as integer
     */
    private int searchSubArrayIndexOfSearchTarget(byte[] searchSubArray, byte[] searchTarget) {
        int index = 0;
        boolean contains = false;
        for (int i = 0; i < searchSubArray.length; i++) {
            for (int j = 0; j < searchTarget.length; j++) {
                if (i + j >= searchSubArray.length) {
                    break;
                }
                if (searchSubArray[i + j] != searchTarget[j]) {
                    contains = false;
                    break;
                } else {
                    contains = true;
                }
            }
            if (contains) {
                index = i;
                return i;
            }
        }
        return index;
    }

}
