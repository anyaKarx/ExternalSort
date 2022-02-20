package anyakar.externalsort;

import anyakar.externalsort.merge.MergeSortedFiles;
import anyakar.externalsort.merge.params.ConsoleParams;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class ConsolePart {


    public static <T extends Comparable<T>> void main(final String[] args) throws IOException {
        Charset cs = Charset.defaultCharset();
        ConsoleParams consoleParams = null;
        while (consoleParams == null) {
            consoleParams = ConsoleParams.parseArgs(args);
        }

        List<File> files = consoleParams.getFiles().stream()
                .map(File::new)
                .collect(Collectors.toList());

        File output = new File(consoleParams.getOutputFile());

        MergeSortedFiles<T> mergeSortedFiles = new MergeSortedFiles<>(consoleParams.getConfigMerge());
        mergeSortedFiles.merge(files, output, cs);

    }
}