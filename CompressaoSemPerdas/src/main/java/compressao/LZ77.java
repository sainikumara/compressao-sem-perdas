package compressao;

import java.util.*;

public class lz77 {

    public static void main(String[] args) {
        String stringToCompress = "The Lempel-Ziv algorithm is a variable-to-fixed length code. Basically, there are two versions of the algorithm presented in the literature: the theoretical version and the practical version. Theoretically, both versions perform essentially the same. However, the proof of the asymptotic optimality of the theoretical version is easier. In practice, the practical version is easier to implement and is slightly more efficient. We explain the practical version of the algorithm as explained in the book by Gersho and Gray and in the paper by Welch. The basic idea is to parse the input sequence into non-overlapping blocks of different lengths while constructing a dictionary of blocks seen thus far. Theoretically, the size of the dictionary can grow infinitely large. In practice, the dictionary size is limited. Once the limit is reached, no more entries are added. Welch had recommended a dictionary of size 4096. This corresponds to 12 bits per index. The length of the index may vary. For example, in the n-th block, the current dictionary size is n+1 (assuming that the limit has not been reached). Therefore, we can encode the index of block n using [log2(n+1)] bits (rounded up to the nearest integer). For example, the first index can be represented by 1 bit, the 2nd and 3rd by 2 bits each, the 4th, 5th, 6th, and 7th by 3 bits each, and so on. This is the variable-to-variable length version of the Lempel-Ziv algorithm. For a maximum dictionary size of 2m, variable-length encoding of the indices saves exactly 2m-1 bits compared to fixed-length encoding. The above example, as most other illustrative examples in the literature, does not result in real compression. Actually, more bits are used to represent the indices than the original data. This is because the length of the input data in the example is too short. In practice, the Lempel-Ziv algorithm works well (lead to actual compression) only when the input data is sufficiently large and there are sufficient redundancy in the data. Decompression works in the reverse fashion. The decoder knows that the last symbol of the most recent dictionary entry is the first symbol of the next parse block. This knowledge is used to resolve possible conflict in the decoder. Many popular programs (e.g. Unix compress and uncompress, gzip and gunzip, and Windows WinZip) are based on the Lempel-Ziv algorithm. The name Ziv-Lempel is more appropriate than Lempel-Ziv (see Gersho and Gray and the name ordering in References 3 and 4 below).";
        int searchWindowLength = 127;
        int lookAheadWindowLength = 7;
        
        System.out.println("Original string: " + stringToCompress);

        ArrayList<DictionaryEntry> dictionary = new lz77().compress(stringToCompress, searchWindowLength, lookAheadWindowLength);
        new lz77().decompress(dictionary);
    }

    ArrayList<DictionaryEntry> compress(String stringToCompress, int searchWindowLength, int lookAheadWindowLength) {
        ArrayList<DictionaryEntry> dictionary = new ArrayList<>();
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
            
            int matchPosition;
            String searchTarget = stringToCompress.substring(charPointer, charPointer + matchingStringLength + 1);

            if (searchSubstring.contains(searchTarget)) {
                matchingStringLength++;
                while (matchingStringLength <= lookAheadWindowLength) {
                    searchTarget = stringToCompress.substring(charPointer, charPointer + matchingStringLength);

                    if (searchSubstring.contains(searchTarget) && ((charPointer + matchingStringLength) < stringToCompress.length())) {
                        matchingStringLength++;
                    } else {
                        break;
                    }
                }
                matchingStringLength--;

                matchPosition = searchSubstring.indexOf(stringToCompress.substring(charPointer, charPointer + matchingStringLength));
                charPointer += matchingStringLength;

                offsetToBeginningOfMatch = (charPointer < (searchWindowLength + matchingStringLength))
                        ? charPointer - matchPosition - matchingStringLength
                        : searchWindowLength - matchPosition;
            }

            char charFollowingMatch = stringToCompress.charAt(charPointer);
            dictionary.add(new DictionaryEntry(offsetToBeginningOfMatch, matchingStringLength, charFollowingMatch));

            charPointer++;
        }
        
        int bitsPerEntry = Integer.bitCount(searchWindowLength) + Integer.bitCount(lookAheadWindowLength) + 8;
        int dictionarySizeInBits = bitsPerEntry * dictionary.size();
        int stringSizeInBits = 8 * stringToCompress.length();
        double relativeCompression = (double) dictionarySizeInBits / stringSizeInBits;
        System.out.println("dictionary size: " + dictionary.size());
        System.out.println("Relative compression: " + relativeCompression);

        return dictionary;
    }

    void decompress(ArrayList<DictionaryEntry> dictionary) {
        StringBuffer reconstructString = new StringBuffer();

        for (DictionaryEntry entry : dictionary) {
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

        System.out.println("Decompressed string: " + new String(reconstructString));
    }

    class DictionaryEntry {

        int offsetToBeginningOfMatch;
        int matchingStringLength;
        char charFollowingMatch;

        DictionaryEntry(int offset, int stringLength, char followingChar) {
            this.offsetToBeginningOfMatch = offset;
            this.matchingStringLength = stringLength;
            this.charFollowingMatch = followingChar;
        }

        @Override
        public String toString() {
            return offsetToBeginningOfMatch + "," + matchingStringLength + "," + charFollowingMatch;
        }
    }
}
