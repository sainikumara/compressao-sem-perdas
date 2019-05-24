package compressao;

import java.util.List;

public class LZ77Decompress {

    /**
     * LZ77 decompression implementation
     *
     * @param dictionary: the compressed form of the original string
     * @return the reconstructed string
     */
    public String decompress(List<LZ77DictionaryEntry> dictionary) {
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
}
