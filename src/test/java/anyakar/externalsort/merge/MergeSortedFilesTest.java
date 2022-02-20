package anyakar.externalsort.merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import anyakar.externalsort.merge.stack.factory.IOStackFactory;
import anyakar.externalsort.merge.stack.factory.StringFileBufferFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MergeSortedFilesTest {

    @Test
    void shouldSort() throws IOException {
        Charset cs = Charset.defaultCharset();

        Comparator<String> comparator = String::compareTo;
        IOStackFactory<String> ioStackFactory = StringFileBufferFactory.getInstance();
        MergeSortedFiles<?> mergeSortedFiles = new MergeSortedFiles<>(comparator, ioStackFactory);
        ClassLoader classLoader = getClass().getClassLoader();
        List<File> files = Arrays.asList(
                new File(classLoader.getResource("str1.txt").getFile()),
                new File(classLoader.getResource("str2.txt").getFile()),
                new File(classLoader.getResource("str3.txt").getFile()));

        File out = new File("output");


        mergeSortedFiles.merge(files, out, cs);
        InputStream in = new FileInputStream(out);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, cs));
        Comparator<String> cmp = Comparator.comparing(String::toString);
        List<String> check = br.lines().collect(Collectors.toList());
        String min = check.get(0);
        for (String current : check) {
            boolean result = (cmp.compare(current, min)) >= 0;
            if (result)
                min = current;
            Assertions.assertEquals(result, true);
        }
    }
}
