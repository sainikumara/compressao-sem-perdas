package compressao;

import java.util.ArrayList;

public class LZ77Compress {

    /**
     * LZ77 compression implementation
     *
     * @param stringToCompress: original string
     * @param searchWindowLength
     * @param lookAheadWindowLength
     * @return dictionary: compressed form of the string
     */
    public ArrayList<LZ77DictionaryEntry> compress(String stringToCompress, int searchWindowLength, int lookAheadWindowLength) {
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
}
