package compressao;

public class CompressionInfo {
    /**
     * Print out info on how the compression succeeded
     * 
     * @param fileToCompress
     * @param compressedFile 
     */
    public void LZ77Info(String fileToCompress, String compressedFile) {
        IO io = new IO();
        byte[] bytesToCompress = io.readBytesFromFile(fileToCompress);
        byte[] bytesFromCompressedFile = io.readBytesFromFile(compressedFile);
        
        double relativeCompression = (double) bytesFromCompressedFile.length / bytesToCompress.length;

        System.out.println("Original file size: " + bytesToCompress.length);
        System.out.println("Compressed data size: " + bytesFromCompressedFile.length);
        System.out.println("Relative compression: " + relativeCompression);
        if (relativeCompression >= 1) {
            System.out.println("Compression was unsuccessful");
        } else {
            System.out.println("Compression was successful");
        }
    }
}
