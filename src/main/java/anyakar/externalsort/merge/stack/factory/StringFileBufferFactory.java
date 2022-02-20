package anyakar.externalsort.merge.stack.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

import anyakar.externalsort.merge.stack.IOStack;
import anyakar.externalsort.merge.stack.StringFileBuffer;

public class StringFileBufferFactory implements IOStackFactory<String> {

    private static final IOStackFactory<? extends Comparable<?>> INSTANCE = new StringFileBufferFactory();

    public static <T extends Comparable<T>> IOStackFactory<T> getInstance() {
        return (IOStackFactory<T>) INSTANCE;
    }

    @Override
    public IOStack<String> create(String fileName, BufferedReader reader, Comparator<String> comparator) throws IOException {
        return new StringFileBuffer(reader, comparator, fileName);
    }
}
