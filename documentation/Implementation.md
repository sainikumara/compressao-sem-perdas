# Implementation

This Lempel-ziv 77 algorithm implementation looks at input data comparing a search window of data that has already been passed and a look-ahead-window that is right ahead of point of reference. At each point it tries to find a match for the next data sequence in the search window, and if that happens, saves the length of the match, the offset its beginning and the byte following the look-ahead-window. If no match is found, it saves the match length as zero and the following byte.

The compressed data is then saved into a file, and can be decompressed using the same application. A successful decompression needs currently the info on the length of the search window and the look-ahead-window, no header containing this information is saved with the compressed data.

The major shortcoming of the current project is that, it doesn't quite reach even the file size of the original file, if the file doesn't contain a lot of reduntant information. Natural language text files and photos are slightly bigger than the original file in the "compressed form". This will hopefully be fixed once the bit packing modules are ready.
