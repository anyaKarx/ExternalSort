package anyakar.externalsort.merge.params;

import java.util.ArrayList;
import java.util.List;


public class ConsoleParams {

    MergeParams configMerge;
    private final List<String> files;
    private final String outputFile;

    public ConsoleParams(boolean reverse, boolean string, List<String> files, String outputFile) {
        this.configMerge = new MergeParams(reverse, string);
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
        int countFiles = 0;

        if (args.length < 4) return null;

        for (int param = 0; param < args.length; ++param) {
            if (args[param].equals("-d")) {
                isReverse = true;
            } else if (args[param].equals("-a")) {
                isReverse = false;
            } else if ((args[param].equals("--help"))) {
                displayUsage();
                return null;
            } else if (args[param].equals("-s") && !isCommandUses) {
                isString = true;
                isCommandUses = true;
            } else if (args[param].equals("-i") && !isCommandUses) {
                isString = false;
                isCommandUses = true;
            } else {
                if (isCommandUses) {
                    if (outputFile == null) {
                        outputFile = args[param];
                    } else {
                        files.add(args[param]);
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


    public MergeParams getConfigMerge() {
        return configMerge;
    }

    public List<String> getFiles() {
        return files;
    }

    public String getOutputFile() {
        return outputFile;
    }


}
