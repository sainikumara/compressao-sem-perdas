package ui;

public class CompressionInfo {
    /**
     * Print out info on how the compression succeeded
     * 
     * @param originalData
     * @param compressedData 
     */
    public void LZ77Info(byte[] originalData, byte[] compressedData) {
        double relativeCompression = (double) compressedData.length / originalData.length;

        System.out.println("Original file size: " + originalData.length);
        System.out.println("Compressed data size: " + compressedData.length);
        System.out.println("Relative compression: " + relativeCompression);
        if (relativeCompression >= 1) {
            System.out.println("Compression was unsuccessful");
        } else {
            System.out.println("Compression was successful");
        }
    }
}
