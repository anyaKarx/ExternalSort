package anyakar.externalsort;

import anyakar.externalsort.console.ConsoleParams;
import anyakar.externalsort.merge.MergeSortedFiles;
import anyakar.externalsort.merge.stack.factory.IOStackFactory;
import anyakar.externalsort.merge.stack.factory.IntegerFileBufferFactory;
import anyakar.externalsort.merge.stack.factory.StringFileBufferFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {

    public static <T extends Comparable<T>> void main(String[] args) throws IOException {
        Charset cs = Charset.defaultCharset();

        ConsoleParams consoleParams = ConsoleParams.parseArgs(args);
        while (consoleParams == null){
            Scanner in = new Scanner(System.in);
            System.out.println("The entered arguments are not correct,");
            System.out.println(" repeat the input, to clarify the commands, call --help\n");
            args = in.nextLine().split("\\s");
            consoleParams = ConsoleParams.parseArgs(args);
        } ;

        List<File> files = consoleParams.getFiles().stream()
                .map(File::new)
                .filter(file -> file.exists())
                .collect(Collectors.toList());

        if (files.isEmpty()) {
            System.out.println("Input files do not exist");
            return;
        }

        File output = new File(consoleParams.getOutputFile());

        if (output.length() != 0) {
            output.delete();
            output = new File(consoleParams.getOutputFile());
        }
        Comparator<T> comparator = Comparable::compareTo;
        if (consoleParams.isReverse()) {
            comparator = Comparator.reverseOrder();
        }

        IOStackFactory<T> ioStackFactory = consoleParams.isString()
                ? StringFileBufferFactory.getInstance()
                : IntegerFileBufferFactory.getInstance();

        MergeSortedFiles<T> mergeSortedFiles = new MergeSortedFiles<>(
                comparator,
                ioStackFactory);

        if (mergeSortedFiles.merge(files, output, cs))
            System.out.println("Sorting completed.");;
    }
}