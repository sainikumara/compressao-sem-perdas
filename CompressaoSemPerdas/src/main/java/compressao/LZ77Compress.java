package compressao;

import util.ArrayCopier;

public class LZ77Compress {

    ArrayCopier arrayCopier = new ArrayCopier();
    byte[] compressedData = new byte[3]; // initialized for testing
    int compressedDataPointer = 0; // initialized for testing
    byte[] searchWindow;
    byte[] searchTarget;
    int originalDataPointer;
    int searchWindowStart;

    /**
     * LZ77 compression implementation for byte array
     *
     * @param originalData: original byte array
     * @param searchWindowLength
     * @param lookAheadWindowLength
     * @return compressedData: compressed data as byte array
     */
    public byte[] compressBytes(byte[] originalData, int searchWindowLength, int lookAheadWindowLength) {
        compressedData = new byte[5 * originalData.length];
        compressedDataPointer = 0;
        originalDataPointer = 0;

        while (originalDataPointer < originalData.length) {
            compressOneSequence(originalData, searchWindowLength, lookAheadWindowLength);
        }
        compressedData = arrayCopier.copyOfRange(compressedData, 0, compressedDataPointer);
        return compressedData;
    }

    /**
     * Compare the contents of the current search window and of the look ahead
     * window of the original data, find the longest match and save the
     * information required for decompressing into the compressed data array
     *
     * @param originalData
     * @param searchWindowMaxLength
     * @param lookAheadWindowMaxLength
     */
    public void compressOneSequence(byte[] originalData, int searchWindowMaxLength, int lookAheadWindowMaxLength) {
        int matchLength = 0;
        int offsetToBeginningOfMatch = 0;
        setSearchWindow(originalData, searchWindowMaxLength);
        setSearchTarget(arrayCopier.copyOfRange(originalData, originalDataPointer, originalDataPointer + 1));

        if (searchWindowContainsSearchTarget()) {
            matchLength = findLongestMatch(originalData, matchLength, lookAheadWindowMaxLength);
            int matchPosition = searchWindowIndexOfSearchTarget();
            offsetToBeginningOfMatch = setOffsetToBeginningOfMatch(searchWindowMaxLength, matchLength, matchPosition);
        }
        addCompressedSequence(offsetToBeginningOfMatch, matchLength, originalData[originalDataPointer]);
    }

    /**
     * Find the longest match of search target inside search window
     *
     * @param originalData
     * @param matchLength
     * @param lookAheadWindowMaxLength
     * @return the length of the longest match
     */
    public int findLongestMatch(byte[] originalData, int matchLength, int lookAheadWindowMaxLength) {
        while (matchLength <= lookAheadWindowMaxLength) {
            searchTarget = arrayCopier.copyOfRange(originalData, originalDataPointer, originalDataPointer + matchLength + 1);

            if (!(originalDataPointer + matchLength + 1 < originalData.length && searchWindowContainsSearchTarget())) {
                break;
            }
            matchLength++;
        }
        searchTarget = arrayCopier.copyOfRange(originalData, originalDataPointer, originalDataPointer + matchLength);
        return matchLength;
    }

    /**
     * Set the search window based on the original data, pointer value for
     * original data and the maximum length for search window
     *
     * @param originalData
     * @param searchWindowMaxLength
     */
    public void setSearchWindow(byte[] originalData, int searchWindowMaxLength) {
        if (originalDataPointer == 0) {
            this.searchWindow = new byte[0];
        }
        searchWindowStart = this.setSearchWindowStart(searchWindowMaxLength);
        this.searchWindow = arrayCopier.copyOfRange(originalData, searchWindowStart, originalDataPointer);
    }

    /**
     * Exists for testing purposes
     *
     * @param newSearchWindow
     */
    public void setSearchWindow(byte[] newSearchWindow) {
        this.searchWindow = newSearchWindow;
    }

    /**
     * Set the target to search based on a given byte array
     *
     * @param newSearchTarget
     */
    public void setSearchTarget(byte[] newSearchTarget) {
        this.searchTarget = newSearchTarget;
    }

    /**
     * Set the starting index for the search window
     *
     * @param searchWindowMaxLength
     * @return the new value for searchWindowStart as integer
     */
    private int setSearchWindowStart(int searchWindowMaxLength) {
        if (originalDataPointer - searchWindowMaxLength >= 0) {
            return originalDataPointer - searchWindowMaxLength;
        }
        return 0;
    }

    public void setOriginalDataPointer(int newPointer) {
        this.originalDataPointer = newPointer;
    }

    /**
     * Find and set the offset to the beginning of match
     *
     * @param searchWindowMaxLength
     * @param matchLength
     * @param matchPosition
     * @return the new value for offsetToBeginningOfMatch as integer
     */
    private int setOffsetToBeginningOfMatch(int searchWindowMaxLength, int matchLength, int matchPosition) {
        originalDataPointer += matchLength;
        if (originalDataPointer < (searchWindowMaxLength + matchLength)) {
            return originalDataPointer - matchPosition - matchLength;
        } else {
            return searchWindowMaxLength - matchPosition;
        }
    }

    /**
     * Check if the current search window contains the current search target
     *
     * @return true if the sub array contains the search target, false otherwise
     */
    public boolean searchWindowContainsSearchTarget() {
        int targetPointer = 0;
        int searchWindowPointer = 0;
        boolean startedFindingTarget = false;

        while (targetPointer < searchTarget.length && searchWindowPointer < searchWindow.length) {
            if (searchTarget[targetPointer] == searchWindow[searchWindowPointer]) {
                startedFindingTarget = true;
                targetPointer++;
            } else if (startedFindingTarget) {
                targetPointer = 0;
                startedFindingTarget = false;
            }
            searchWindowPointer++;
        }
        return targetPointer == searchTarget.length;
    }

    /**
     * Find the first index of the search target within the search window
     *
     * @return the index as integer
     */
    public int searchWindowIndexOfSearchTarget() {
        boolean contains = false;
        for (int i = 0; i < searchWindow.length; i++) {
            for (int j = 0; j < searchTarget.length; j++) {
                if (i + j >= searchWindow.length) {
                    break;
                }
                if (searchWindow[i + j] != searchTarget[j]) {
                    contains = false;
                    break;
                }
                contains = true;
            }
            if (contains) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Add the compressed data related to the handled sequence into the array
     * containing all compressed data
     *
     * @param offsetToBeginningOfMatch
     * @param matchLength
     * @param followingByte
     */
    private void addLessCompressedSequence(int offsetToBeginningOfMatch, int matchLength, byte followingByte) {
        compressedData[compressedDataPointer++] = (byte) offsetToBeginningOfMatch;
        compressedData[compressedDataPointer++] = (byte) matchLength;
        compressedData[compressedDataPointer++] = followingByte;
        originalDataPointer++;
    }

    /**
     * Add the compressed data related to the handled sequence into the array
     * containing all compressed data
     *
     * @param offsetToBeginningOfMatch
     * @param matchLength
     * @param followingByte
     */
    private void addCompressedSequence(int offsetToBeginningOfMatch, int matchLength, byte followingByte) {
        compressedData[compressedDataPointer++] = (byte) matchLength;
        if (matchLength == 0) {
            compressedData[compressedDataPointer++] = followingByte;
        } else {
            compressedData[compressedDataPointer++] = (byte) offsetToBeginningOfMatch;
            compressedData[compressedDataPointer++] = followingByte;
        }

        originalDataPointer++;
    }

    /**
     * Exists for testing purposes
     *
     * @return compressedData
     */
    public byte[] getCompressedData() {
        return this.compressedData;
    }
}
