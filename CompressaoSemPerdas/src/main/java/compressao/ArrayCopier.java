package compressao;

public class ArrayCopier {

    /**
     * Create a copy of a certain range of original byte array
     *
     * @param originalArray
     * @param beginning
     * @param end
     * @return new byte array that is a copy of the specified range in the
     * original
     */
    public byte[] copyOfRange(byte[] originalArray, int beginning, int end) {
        byte[] newArray = new byte[end - beginning];

        for (int i = 0; i < end - beginning; i++) {
            newArray[i] = originalArray[beginning + i];
        }

        return newArray;
    }
}
