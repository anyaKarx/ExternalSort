package anyakar.externalsort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import anyakar.externalsort.console.ConsoleParams;
import anyakar.externalsort.merge.MergeSortedFiles;
import anyakar.externalsort.merge.stack.factory.IOStackFactory;
import anyakar.externalsort.merge.stack.factory.IntegerFileBufferFactory;
import anyakar.externalsort.merge.stack.factory.StringFileBufferFactory;

public class Application {

    public static <T extends Comparable<T>> void main( String[] args) throws IOException {
        Charset cs = Charset.defaultCharset();
        Scanner in = new Scanner(System.in);
        ConsoleParams consoleParams = null;
                while(consoleParams == null){
                   consoleParams = consoleParams.parseArgs(args);
                    args = in.nextLine().split("\\s");
                }
        List<File> files = consoleParams.getFiles().stream()
                .map(File::new)
                .filter(file -> file.exists())
                .collect(Collectors.toList());

         if (files.isEmpty())
         {
             System.out.println("Input files do not exist");
             return;
         }

        File output = new File(consoleParams.getOutputFile());

         if(output.length() != 0) {
             output.delete();
             output = new File(consoleParams.getOutputFile());
         }
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