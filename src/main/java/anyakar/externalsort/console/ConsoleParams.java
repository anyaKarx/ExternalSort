package anyakar.externalsort.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import anyakar.externalsort.merge.stack.factory.IOStackFactory;
import anyakar.externalsort.merge.stack.factory.StringFileBufferFactory;


public class ConsoleParams {

    private final boolean reverse;
    private final boolean string;
    private final List<String> files;
    private final String outputFile;

    public ConsoleParams(boolean reverse, boolean string, List<String> files, String outputFile) {
        this.reverse = reverse;
        this.string = string;
        this.files = files;
        this.outputFile = outputFile;
    }

    private static void displayUsage() {
        System.out.println("Flags are:");
        System.out.println("-a sort by ascending");
        System.out.println("-d reversed sort");
        System.out.println("-s type is string");
        System.out.println("-i type is Integer");

    }

    public static ConsoleParams parseArgs(String[] args) {

        boolean isReverse = false;
        boolean isString = false;
        boolean isCommandUses = false;
        List<String> files = new ArrayList<String>();
        String outputFile = null;

        if (args.length < 4) return null;

        for (String arg : args) {
            if (arg.equals("-d")) {
                isReverse = true;
            } else if (arg.equals("-a")) {
                isReverse = false;
            } else if ((arg.equals("--help"))) {
                displayUsage();
                return null;
            } else if (arg.equals("-s") && !isCommandUses) {
                isString = true;
                isCommandUses = true;
            } else if (arg.equals("-i") && !isCommandUses) {
                isString = false;
                isCommandUses = true;
            } else {
                if (isCommandUses) {
                    if (outputFile == null) {
                        outputFile = arg;
                    } else {
                        files.add(arg);
                    }
                } else return null;
            }
        }
        if (outputFile == null) {
            System.out.println("please provide input and output file names");
            displayUsage();
            return null;
        }

        return new ConsoleParams(isReverse, isString, files, outputFile);
    }


    public List<String> getFiles() {
        return files;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public boolean isReverse() {
        return reverse;
    }

    public boolean isString() {
        return string;
    }
}
