# Testing documentation

## Unit testing

Run tests by writing

```
mvn test
```

in project root. After that, you can create a test coverage report by running 

```
mvn jacoco:report
```

You can see the report by opening _target/site/jacoco/index.html_ on a browser.

Jacoco report for the project shows 93 % test coverage. User interface classes have been left out of the report.

<img src="https://github.com/sainikumara/compressao_sem_perdas/blob/master/documentation/jacoco_2019-06-20.png" width="850">

## Testing compression

After running the program using a test file, you can observe the file sizes (for example) by running

```
ls -lh *
```

Currently this is likely to reveal that the packaging does not in fact produce a smaller file size, but that will hopefully be fixed soon.

The current performance on different natural language text files can be observed in this chart:

<img src="https://github.com/sainikumara/compressao_sem_perdas/blob/master/documentation/performance.png" width="500">
