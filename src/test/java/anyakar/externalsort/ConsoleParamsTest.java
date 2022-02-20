package anyakar.externalsort;

import anyakar.externalsort.console.ConsoleParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConsoleParamsTest {

    @Test
    void shouldParseArgs() {
        final String[] args = new String[]{"-i", "result.txt", "input", "input2"};

        final ConsoleParams params = ConsoleParams.parseArgs(args);

        Assertions.assertEquals(params.isReverse(), false);
        Assertions.assertEquals(params.isString(), false);
        Assertions.assertEquals(params.getOutputFile(), "result.txt");
        Assertions.assertEquals(params.getFiles().get(0), "input");
        Assertions.assertEquals(params.getFiles().get(1), "input2");
    }
}