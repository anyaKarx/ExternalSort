package anyakar.externalsort.merge;

import anyakar.externalsort.merge.stack.IOStack;
import anyakar.externalsort.merge.stack.factory.IOStackFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSortedFiles<T extends Comparable<T>> {

    private final Comparator<T> comparator;
    private final IOStackFactory<T> ioStackFactory;

    public MergeSortedFiles(Comparator<T> comparator, IOStackFactory<T> ioStackFactory) {
        this.comparator = comparator;
        this.ioStackFactory = ioStackFactory;
    }

    private boolean merge(BufferedWriter bufferedWriter, List<IOStack<T>> buffers) throws IOException {
        try (bufferedWriter) {
            while (true) {
                T tempLine = null;
                int index = -1;
                for (var buffer : buffers) {
                    if (priorityCheck(buffer.peek(), tempLine)) {
                        tempLine = buffer.peek();
                        index = buffers.indexOf(buffer);
                    }
                }
                if (index > -1) {
                    bufferedWriter.write(buffers.get(index).pop().toString());
                    bufferedWriter.newLine();
                } else {
                    break;
                }
            }
            return true;
        } finally {
            for (IOStack<T> buffer : buffers) {
                buffer.close();
            }
        }
    }

    private boolean priorityCheck(T peek, T tempLine) {
        if (peek == null)
            return false;
        else if (tempLine == null)
            return true;
        else
            return comparator.compare(peek, tempLine) <= 0;
    }

    public boolean merge(List<File> files, File outputFile, Charset cs) throws IOException {
        List<IOStack<T>> listFileBuffer = new ArrayList<>();
        for (File f : files) {
            if (f.length() == 0) {
                continue;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f), cs));
            IOStack<T> buffer = ioStackFactory.create(f.getName(), bufferedReader, comparator);
            listFileBuffer.add(buffer);
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile, true), cs));
        return merge(bufferedWriter, listFileBuffer);
    }
}

