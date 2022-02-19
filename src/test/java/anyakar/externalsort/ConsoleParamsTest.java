package anyakar.externalsort;

import anyakar.externalsort.merge.MergeSortedFiles;
import anyakar.externalsort.merge.params.ConsoleParams;
import anyakar.externalsort.merge.params.MergeParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class ConsoleParamsTest {

    @Test
    void shouldParseArgs() {
        // given
        final String[] args = new String[]{"-i", "output", "input", "input2"};

        // when
        final ConsoleParams params = ConsoleParams.parseArgs(args);

        // then
//        Assertions.assertEquals(params, new ConsoleParams(false, false, Collections.singletonList("input"), "output"));
        Assertions.assertEquals(params.getConfigMerge().isReverse(), false);
        Assertions.assertEquals(params.getConfigMerge().isString(), false);
        Assertions.assertEquals(params.getOutputFile(), "output");
        Assertions.assertEquals(params.getFiles().get(0), "input");
        Assertions.assertEquals(params.getFiles().get(1), "input2");
    }

    @Test
    void shouldSort() throws IOException {
        MergeSortedFiles mergeSortedFiles = new MergeSortedFiles(new MergeParams(false, false));

        List<File> files = Arrays.asList(
                new File("/Users/anyakar/Documents/ExternalSort/src/test/resources/in1"),
                new File("/Users/anyakar/Documents/ExternalSort/src/test/resources/in2"));

        File myOutput = new File("result.txt");

        File out = new File("output");

        mergeSortedFiles.merge(files, out);

        Assertions.assertEquals(out, myOutput);
    }
}