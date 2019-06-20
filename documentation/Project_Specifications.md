# Project Specifications

This project aims to implement the lossless data compression algorithm [Lempel-Ziv (LZ77)](https://en.wikipedia.org/wiki/LZ77_and_LZ78).

The resulting program will take a file as input and give a compressed version of the file as output. It can also reverse the compression. LZ77 algorithms achieve compression by replacing repeated occurrences of data with references to a single copy of that data existing earlier in the uncompressed data stream.

The target time complexity for LZ77 is O(n), because in the worst case scenario the whole process has to be run n times, because no matches are found. Also in this worst case scenario, the space complexity is O(n), because the dictionary will have an entry for each character of the input.
