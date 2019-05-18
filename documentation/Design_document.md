# Project Specifications

This project aims to implement the lossless data compression algorithm [Deflate](https://en.wikipedia.org/wiki/DEFLATE) by first implementing [Lempel-Ziv (LZ77)](https://en.wikipedia.org/wiki/LZ77_and_LZ78) and combining it with [Huffman coding](https://en.wikipedia.org/wiki/Huffman_coding).

The resulting program will take a string file as input and give a compressed version of the file as output. It can also reverse the compression. LZ77 algorithms achieve compression by replacing repeated occurrences of data with references to a single copy of that data existing earlier in the uncompressed data stream. The LZ77 creates a dictionary, which is then further compressed by using Huffman coding.

The target time complexity for LZ77 is O(n), because in the worst case scenario the whole process has to be run n times, because no matches are found. Also in this worst case scenario, the space complexity is O(n), because the dictionary will have an entry for each character of the input. Huffman encoding can also be implemented in O(n) time and space.
