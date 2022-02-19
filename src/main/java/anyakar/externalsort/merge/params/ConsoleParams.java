package anyakar.externalsort.merge.params;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import anyakar.externalsort.merge.params.MergeParams;
import com.beust.jcommander.Parameter;


public class ConsoleParams {

    MergeParams configMerge;
    private final List<String> files;
    private final String outputFile;

    public ConsoleParams(boolean reverse, boolean string, List<String> files, String outputFile) {
        this.configMerge= new MergeParams(reverse, string);
        this.files = files;
        this.outputFile = outputFile;
    }

    private static void displayUsage() {
        System.out.println("Flags are:");
        System.out.println("-a or --verbose: verbose output");
        System.out.println("-d reversed sort");
        System.out.println("-s type is string");
        System.out.println("-i type is Integer");
        System.out
                .println("-t or --maxtmpfiles (followed by an integer): specify an upper bound on the number of temporary files");

    }

    public static ConsoleParams parseArgs(String[] args) {

        boolean isReverse = false;
        boolean isString = false;
        boolean isCommandUses = false;
        boolean isCommandUses2 = false;
        List<String> files =   new ArrayList<String>();
        String outputFile = null;
        int countFiles = 0;

        if (args.length < 4)
            return null;

        for (int param = 0; param < args.length; ++param) {
            if (args[param].equals("-d") && !isCommandUses) {
                isReverse = true;
                isCommandUses = true;
            }else if (args[param].equals("-a") && !isCommandUses) {
                isReverse = false;
                isCommandUses = true;
            } else if ((args[param].equals("--help"))) {
                displayUsage();
                return null;
            } else if (args[param].equals("-s") && !isCommandUses2) {
                isString = true;
                isCommandUses2 = true;
            } else if (args[param].equals("-i")&& !isCommandUses2) {
                isString=false;
                isCommandUses2 = true;
            } else {
                if (isCommandUses && isCommandUses2)
                {
                    if (outputFile == null) {
                        outputFile = args[param];
                    } else {
                        files.add(args[param]);
                    }
                } else
                    return null;
            }
        }
        if (outputFile == null) {
            System.out
                    .println("please provide input and output file names");
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
