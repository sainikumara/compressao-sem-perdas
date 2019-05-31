package compressao;

import java.util.ArrayList;
import java.util.Arrays;

public class LZ77Compress {

    /**
     * LZ77 compression implementation for String
     *
     * @param stringToCompress: original string
     * @param searchWindowLength
     * @param lookAheadWindowLength
     * @return dictionary: compressed form of the string
     */
    public ArrayList<LZ77DictionaryEntry> compressString(String stringToCompress, int searchWindowLength, int lookAheadWindowLength) {
        ArrayList<LZ77DictionaryEntry> dictionary = new ArrayList<>();
        String searchSubstring;

        int charPointer = 0;
        int searchWindowStart;

        while (charPointer < stringToCompress.length()) {
            int offsetToBeginningOfMatch = 0;
            int matchingStringLength = 0;

            searchWindowStart = (charPointer - searchWindowLength >= 0)
                    ? charPointer - searchWindowLength
                    : 0;

            if (charPointer == 0) {
                searchSubstring = "";
            } else {
                searchSubstring = stringToCompress.substring(searchWindowStart, charPointer);
            }

            String searchTarget = stringToCompress.substring(charPointer, charPointer + 1);

            if (searchSubstring.contains(searchTarget)) {

                while (matchingStringLength <= lookAheadWindowLength) {
                    searchTarget = stringToCompress.substring(charPointer, charPointer + matchingStringLength + 1);

                    if (searchSubstring.contains(searchTarget) && ((charPointer + matchingStringLength + 1) < stringToCompress.length())) {
                        matchingStringLength++;
                    } else {
                        break;
                    }
                }

                int matchPosition = searchSubstring.indexOf(stringToCompress.substring(charPointer, charPointer + matchingStringLength));
                charPointer += matchingStringLength;

                offsetToBeginningOfMatch = (charPointer < (searchWindowLength + matchingStringLength))
                        ? charPointer - matchPosition - matchingStringLength
                        : searchWindowLength - matchPosition;
            }

            char charFollowingMatch = stringToCompress.charAt(charPointer);
            dictionary.add(new LZ77DictionaryEntry(offsetToBeginningOfMatch, matchingStringLength, charFollowingMatch));

            charPointer++;
        }

        return dictionary;
    }

    /**
     * LZ77 compression implementation for byte array
     * 
     * @param bytesToCompress: original byte array
     * @param searchWindowLength
     * @param lookAheadWindowLength
     * @return dictionary: compressed form as byte array
     */
    public byte[] compressBytes(byte[] bytesToCompress, int searchWindowLength, int lookAheadWindowLength) {
        byte[] dictionary = new byte[3 * bytesToCompress.length];
        int dictionaryPointer = 0;
        byte[] searchSubArray;

        int bytePointer = 0;
        int searchWindowStart;

        while (bytePointer < bytesToCompress.length) {
            int offsetToBeginningOfMatch = 0;
            int matchingBytesLength = 0;

            searchWindowStart = (bytePointer - searchWindowLength >= 0)
                    ? bytePointer - searchWindowLength
                    : 0;

            if (bytePointer == 0) {
                searchSubArray = new byte[0];
            } else {
                searchSubArray = Arrays.copyOfRange(bytesToCompress, searchWindowStart, bytePointer);
            }

            byte[] searchTarget = Arrays.copyOfRange(bytesToCompress, bytePointer, bytePointer + 1);

            if (searchSubArrayContainsSearchTarget(searchSubArray, searchTarget)) {
                while (matchingBytesLength <= lookAheadWindowLength) {
                    searchTarget = Arrays.copyOfRange(bytesToCompress, bytePointer, bytePointer + matchingBytesLength + 1);

                    if (searchSubArrayContainsSearchTarget(searchSubArray, searchTarget)
                            && ((bytePointer + matchingBytesLength + 1) < bytesToCompress.length)) {
                        matchingBytesLength++;
                    } else {
                        break;
                    }
                }
                
                int matchPosition = searchSubArrayIndexOfSearchTarget(searchSubArray, Arrays.copyOfRange(bytesToCompress, bytePointer, bytePointer + matchingBytesLength));
                bytePointer += matchingBytesLength;

                offsetToBeginningOfMatch = (bytePointer < (searchWindowLength + matchingBytesLength))
                        ? bytePointer - matchPosition - matchingBytesLength
                        : searchWindowLength - matchPosition;
            }

            dictionary[dictionaryPointer++] = (byte) offsetToBeginningOfMatch;
            dictionary[dictionaryPointer++] = (byte) matchingBytesLength;
            dictionary[dictionaryPointer++] = bytesToCompress[bytePointer];
            
            bytePointer++;
        }
        
        dictionary = Arrays.copyOfRange(dictionary, 0, dictionaryPointer);

        return dictionary;
    }

    /**
     * Check if the sub array contains the search target
     * 
     * @param searchSubArray
     * @param searchTarget
     * @return true if the sub array contains the search target, false otherwise
     */
    private boolean searchSubArrayContainsSearchTarget(byte[] searchSubArray, byte[] searchTarget) {
        int targetPointer = 0;
        int searchWindowPointer = 0;
        boolean startedFindingTarget = false;
        
        while (targetPointer < searchTarget.length && searchWindowPointer < searchSubArray.length) {
            if (searchTarget[targetPointer] != searchSubArray[searchWindowPointer]) {
                if (startedFindingTarget) {
                    break;
                }
                searchWindowPointer++;
            } else {
                searchWindowPointer++;
                targetPointer++;
                startedFindingTarget = true;
            }
        }
        return targetPointer == searchTarget.length;
    }
    
    /**
     * Find the first index of the search target within the sub array
     * 
     * @param searchSubArray
     * @param searchTarget
     * @return the index as int
     */
    private int searchSubArrayIndexOfSearchTarget(byte[] searchSubArray, byte[] searchTarget) {
        int index = 0;
        boolean contains = false;
        for (int i = 0; i < searchSubArray.length; i++) {
            for (int j = 0; j < searchTarget.length; j++) {
                if (i + j >= searchSubArray.length) {
                    break;
                }
                if (searchSubArray[i+j] != searchTarget[j]) {
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
