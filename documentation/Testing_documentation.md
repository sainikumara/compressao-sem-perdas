#Testing documentation

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

## Testing compression

After running the program using a test file, you can observe the file sizes (for example) by running

```
ls -lh *
```

Currently this is likely to reveal that the packaging does not in fact produce a smaller file size, but that will hopefully be fixed soon.