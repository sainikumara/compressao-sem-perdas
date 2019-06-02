package compressao;

/**
 * Wrapper class for one entry in the compressed form of the data used in LZ77 compression of String form data
 */
public class LZ77CompressedEntry {

    int offsetToBeginningOfMatch;
    int matchingStringLength;
    char charFollowingMatch;

    LZ77CompressedEntry(int offset, int stringLength, char followingChar) {
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
