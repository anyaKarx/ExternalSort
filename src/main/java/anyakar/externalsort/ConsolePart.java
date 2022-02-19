package anyakar.externalsort;

import anyakar.externalsort.merge.comparator.ComparatorFactory;
import anyakar.externalsort.merge.params.ConsoleParams;
import anyakar.externalsort.merge.MergeSortedFiles;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsolePart {



    public static <T extends Comparable<T>> void main(final String[] args) throws IOException {
        ConsoleParams consoleParams = null; // test it
        while (consoleParams == null)
        {
            consoleParams = ConsoleParams.parseArgs(args);
        }

        List<File> files = consoleParams.getFiles().stream()
                .map(File::new)
                .collect(Collectors.toList());

        File output = new File(consoleParams.getOutputFile());

        MergeSortedFiles<T> mergeSortedFiles = new MergeSortedFiles<>( consoleParams.getConfigMerge());
        mergeSortedFiles.merge(files, output); // test it

    }
}