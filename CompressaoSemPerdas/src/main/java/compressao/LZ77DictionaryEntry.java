package compressao;

/**
 * Wrapper class for one entry in the dictionary used in LZ77 compression
 */
public class LZ77DictionaryEntry {

    int offsetToBeginningOfMatch;
    int matchingStringLength;
    char charFollowingMatch;

    LZ77DictionaryEntry(int offset, int stringLength, char followingChar) {
        this.offsetToBeginningOfMatch = offset;
        this.matchingStringLength = stringLength;
        this.charFollowingMatch = followingChar;
    }

    public int getOffset() {
        return this.offsetToBeginningOfMatch;
    }

    public int getmatchLength() {
        return this.matchingStringLength;
    }

    public char getFollowingChar() {
        return this.charFollowingMatch;
    }

    @Override
    public String toString() {
        return offsetToBeginningOfMatch + "," + matchingStringLength + "," + charFollowingMatch;
    }
}
