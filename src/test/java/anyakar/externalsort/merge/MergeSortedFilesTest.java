package anyakar.externalsort.merge;

import anyakar.externalsort.merge.stack.factory.IOStackFactory;
import anyakar.externalsort.merge.stack.factory.IntegerFileBufferFactory;
import anyakar.externalsort.merge.stack.factory.StringFileBufferFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class MergeSortedFilesTest {

    @Test
    void reverse() throws IOException {
        Charset cs = Charset.defaultCharset();

        Comparator<Integer> comparator = Integer::compareTo;
        IOStackFactory<Integer> ioStackFactory = IntegerFileBufferFactory.getInstance();
        MergeSortedFiles<?> mergeSortedFiles = new MergeSortedFiles<>(comparator.reversed(), ioStackFactory);
        ClassLoader classLoader = getClass().getClassLoader();
        List<File> files = Arrays.asList(
                new File(Objects.requireNonNull(classLoader.getResource("int1")).getFile()),
                new File(Objects.requireNonNull(classLoader.getResource("int2")).getFile()));

        File out = new File("output");
        if (out.length() != 0) {
            out.delete();
            out = new File("output");
        }

        mergeSortedFiles.merge(files, out, cs);
        InputStream in = new FileInputStream(out);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, cs));
        Comparator<Integer> cmp = Integer::compareTo;
        cmp = cmp.reversed();
        List<Integer> check = new ArrayList<>();
        for (int i = 0; i <1000; i++)
        {
            check.add(Integer.parseInt(br.readLine()));
        }
        Integer min = check.get(0);
        for (Integer current : check) {
            boolean result = (cmp.compare(current, min)) >= 0;
            if (result)
                min = current;
            Assertions.assertTrue(result);
        }
    }

    @Test
    void getTestFileAhahah() throws IOException {
        Charset cs = Charset.defaultCharset();
        File int1 = new File("int2");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(int1, true), cs));

        try {
            for (int i = 1000000; i>0; i--) {
                bufferedWriter.write(Integer.toString(i));
                bufferedWriter.newLine();
                i--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(int1.canRead());
    }

    @Test
    void shouldSort() throws IOException {
        Charset cs = Charset.defaultCharset();

        Comparator<String> comparator = String::compareTo;
        IOStackFactory<String> ioStackFactory = StringFileBufferFactory.getInstance();
        MergeSortedFiles<?> mergeSortedFiles = new MergeSortedFiles<>(comparator.reversed(), ioStackFactory);
        ClassLoader classLoader = getClass().getClassLoader();
        List<File> files = Arrays.asList(
                new File(Objects.requireNonNull(classLoader.getResource("str1.txt")).getFile()),
                new File(Objects.requireNonNull(classLoader.getResource("str2.txt")).getFile()),
                new File(Objects.requireNonNull(classLoader.getResource("str3.txt")).getFile()));

        File out = new File("output");
        if (out.length() != 0) {
            out.delete();
            out = new File("output");
        }

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
            Assertions.assertTrue(result);
        }
    }
}
