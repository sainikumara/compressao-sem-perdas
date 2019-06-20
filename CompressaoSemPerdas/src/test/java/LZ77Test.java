
import compressao.LZ77Compress;
import compressao.LZ77Decompress;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LZ77Test {

    String string1ToCompress;
    String string2ToCompress;
    byte[] string1AsBytes;
    byte[] string2AsBytes;
    int searchWindowLength;
    int lookAheadWindowLength;
    LZ77Compress compressor;
    LZ77Decompress decompressor;

    public LZ77Test() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        string1ToCompress = "The Lempel-Ziv algorithm is a variable-to-fixed length code. Basically, there are two versions of the algorithm presented in the literature: the theoretical version and the practical version. Theoretically, both versions perform essentially the same. However, the proof of the asymptotic optimality of the theoretical version is easier. In practice, the practical version is easier to implement and is slightly more efficient. We explain the practical version of the algorithm as explained in the book by Gersho and Gray and in the paper by Welch. The basic idea is to parse the input sequence into non-overlapping blocks of different lengths while constructing a dictionary of blocks seen thus far. Theoretically, the size of the dictionary can grow infinitely large. In practice, the dictionary size is limited. Once the limit is reached, no more entries are added. Welch had recommended a dictionary of size 4096. This corresponds to 12 bits per index. The length of the index may vary. For example, in the n-th block, the current dictionary size is n+1 (assuming that the limit has not been reached). Therefore, we can encode the index of block n using [log2(n+1)] bits (rounded up to the nearest integer). For example, the first index can be represented by 1 bit, the 2nd and 3rd by 2 bits each, the 4th, 5th, 6th, and 7th by 3 bits each, and so on. This is the variable-to-variable length version of the Lempel-Ziv algorithm. For a maximum dictionary size of 2m, variable-length encoding of the indices saves exactly 2m-1 bits compared to fixed-length encoding. The above example, as most other illustrative examples in the literature, does not result in real compression. Actually, more bits are used to represent the indices than the original data. This is because the length of the input data in the example is too short. In practice, the Lempel-Ziv algorithm works well (lead to actual compression) only when the input data is sufficiently large and there are sufficient redundancy in the data. Decompression works in the reverse fashion. The decoder knows that the last symbol of the most recent dictionary entry is the first symbol of the next parse block. This knowledge is used to resolve possible conflict in the decoder. Many popular programs (e.g. Unix compress and uncompress, gzip and gunzip, and Windows WinZip) are based on the Lempel-Ziv algorithm. The name Ziv-Lempel is more appropriate than Lempel-Ziv (see Gersho and Gray and the name ordering in References 3 and 4 below).";
        string2ToCompress = "Opiskelija ratkaisee ohjaajan kanssa sovitun ohjelmointiongelman ja toteuttaa itse ratkaisualgoritmin ja sen aputietorakenteet Java-kielellä. Opiskelija testaa ja dokumentoi ratkaisunsa. Ohjaajan kanssa sovitun tehtävänannon mukaisesti työhön kuuluu lisäksi esim. itsenäistä kirjallisuuteen perustuvaa selvitystä tai algoritmien kokeellista vertailua. Oppimateriaali Harjoitustyön apuna käytetään valittuun aiheeseen liittyvää algoritmialan kirjallisuutta ja tarpeen mukaan ohjelmointikielen dokumentaatiota jne. Oppimista tukevat aktiviteetit ja opetusmenetelmät Opiskelija raportoi viikoittain työn edistymisestä ohjaajalle ja saa siitä palautetta. Ohjaaja antaa tarpeen mukaan neuvoja henkilökohtaisesti tai verkon välityksellä. Opiskelija antaa ja saa myös vertaispalautetta.";
        string1AsBytes = string1ToCompress.getBytes();
        string2AsBytes = string2ToCompress.getBytes();

        searchWindowLength = 127;
        lookAheadWindowLength = 7;
        compressor = new LZ77Compress();
        decompressor = new LZ77Decompress();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void compressOneSequenceWorks() {
        byte[] data = "acab a abc".getBytes();
        compressor.setOriginalDataPointer(7);
        compressor.compressOneSequence(data, 127, 7);
        byte[] compressedData = compressor.getCompressedData();

        assertEquals(5, compressedData[0]);
        assertEquals(2, compressedData[1]);
        assertEquals('c', (char) compressedData[2]);
    }

    @Test
    public void longestMatchCorrectLength() {
        byte[] data = "acab a abc".getBytes();
        byte[] window = "acab a ".getBytes();
        compressor.setOriginalDataPointer(7);
        compressor.setSearchWindow(window);

        assertEquals(2, compressor.findLongestMatch(data, 0, 7));

        window = "acab ".getBytes();
        compressor.setOriginalDataPointer(5);
        compressor.setSearchWindow(window);

        assertEquals(1, compressor.findLongestMatch(data, 0, 7));
    }

    @Test
    public void searchWindowContainsSearchTargetWorks() {
        byte[] window = "a abc".getBytes();
        byte[] target1 = "a".getBytes();
        byte[] target2 = "ab".getBytes();
        byte[] target3 = "ac".getBytes();

        compressor.setSearchWindow(window);

        compressor.setSearchTarget(target1);
        assertTrue(compressor.searchWindowContainsSearchTarget());
        compressor.setSearchTarget(target2);
        assertTrue(compressor.searchWindowContainsSearchTarget());
        compressor.setSearchTarget(target3);
        assertFalse(compressor.searchWindowContainsSearchTarget());
    }

    @Test
    public void searchWindowIndexOfSearchTargetWorks() {
        byte[] window = "a abc".getBytes();
        byte[] target1 = "a".getBytes();
        byte[] target2 = "ab".getBytes();
        byte[] target3 = "ac".getBytes();

        compressor.setSearchWindow(window);

        compressor.setSearchTarget(target1);
        assertEquals(0, compressor.searchWindowIndexOfSearchTarget());
        compressor.setSearchTarget(target2);
        assertEquals(2, compressor.searchWindowIndexOfSearchTarget());
        compressor.setSearchTarget(target3);
        assertEquals(-1, compressor.searchWindowIndexOfSearchTarget());
    }

    @Test
    public void correctAmountOfEntriesInCompressedData() {
        byte[] compressed1 = compressor.compressBytes(string1AsBytes, searchWindowLength, lookAheadWindowLength);
        int compressed1Size = compressed1.length;
        byte[] compressed2 = compressor.compressBytes(string2AsBytes, searchWindowLength, lookAheadWindowLength);
        int compressed2Size = compressed2.length;

        assertEquals(2580, compressed1Size);
        assertEquals(918, compressed2Size);
    }

    @Test
    public void compressionIsLossless() {
        byte[] compressed1 = compressor.compressBytes(string1AsBytes, searchWindowLength, lookAheadWindowLength);
        byte[] compressed2 = compressor.compressBytes(string2AsBytes, searchWindowLength, lookAheadWindowLength);
        String decompressedString1 = "";
        String decompressedString2 = "";

        try {
            decompressedString1 = new String(decompressor.decompressData(compressed1));
            decompressedString2 = new String(decompressor.decompressData(compressed2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(string1ToCompress, decompressedString1);
        assertEquals(string2ToCompress, decompressedString2);
    }
}
