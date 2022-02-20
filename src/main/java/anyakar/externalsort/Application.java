package anyakar.externalsort;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import anyakar.externalsort.console.ConsoleParams;
import anyakar.externalsort.merge.MergeSortedFiles;
import anyakar.externalsort.merge.stack.factory.IOStackFactory;
import anyakar.externalsort.merge.stack.factory.IntegerFileBufferFactory;
import anyakar.externalsort.merge.stack.factory.StringFileBufferFactory;

public class Application {

    public static <T extends Comparable<T>> void main(final String[] args) throws IOException {
        Charset cs = Charset.defaultCharset();
        ConsoleParams consoleParams = ConsoleParams.parseArgs(args);
        List<File> files = consoleParams.getFiles().stream()
                .map(File::new)
                .collect(Collectors.toList());

        File output = new File(consoleParams.getOutputFile());

        Comparator<T> comparator = Comparable::compareTo;
        if (consoleParams.isReverse()) {
            comparator.reversed();
        }

        IOStackFactory<T> ioStackFactory = consoleParams.isString()
                ? StringFileBufferFactory.getInstance()
                : IntegerFileBufferFactory.getInstance();

        MergeSortedFiles<T> mergeSortedFiles = new MergeSortedFiles<>(
                comparator,
                ioStackFactory);

        mergeSortedFiles.merge(files, output, cs);
    }
}