package anyakar.externalsort;

import anyakar.externalsort.merge.MergeSortedFiles;
import anyakar.externalsort.merge.params.ConsoleParams;
import anyakar.externalsort.merge.params.MergeParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class ConsoleParamsTest {

    @Test
    void shouldParseArgs() {
        final String[] args = new String[]{"-i", "result.txt", "input", "input2"};

        final ConsoleParams params = ConsoleParams.parseArgs(args);

        Assertions.assertEquals(params.getConfigMerge().isReverse(), false);
        Assertions.assertEquals(params.getConfigMerge().isString(), false);
        Assertions.assertEquals(params.getOutputFile(), "result.txt");
        Assertions.assertEquals(params.getFiles().get(0), "input");
        Assertions.assertEquals(params.getFiles().get(1), "input2");
    }

    @Test
    void shouldSort() throws IOException, InterruptedException {
        Charset cs = Charset.defaultCharset();
        MergeSortedFiles mergeSortedFiles = new MergeSortedFiles(new MergeParams(false, true));

        List<File> files = Arrays.asList(
                new File("/Users/anyakar/Documents/ExternalSort/src/test/resources/payloads/str1.txt"),
                new File("/Users/anyakar/Documents/ExternalSort/src/test/resources/payloads/str2.txt"),
                new File("/Users/anyakar/Documents/ExternalSort/src/test/resources/payloads/str3.txt"));

        File out = new File("output");


        mergeSortedFiles.merge(files, out, cs);
        InputStream in = new FileInputStream(out);
        BufferedReader br= new BufferedReader(new InputStreamReader(in, cs));
        Comparator<String> cmp = Comparator.comparing(String::toString);
        List<String> check = br.lines().collect(Collectors.toList());
        String min = check.get(0);
        for (String current: check)
        {
          boolean result = (cmp.compare(current, min)) >= 0;
            if (result)
                min = current;
            Assertions.assertEquals(result, true);
        }
    }
}