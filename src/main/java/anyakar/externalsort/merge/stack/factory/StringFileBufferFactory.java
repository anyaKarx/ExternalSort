package anyakar.externalsort.merge.stack.factory;

import anyakar.externalsort.merge.stack.IOStack;
import anyakar.externalsort.merge.stack.StringFileBuffer;

import java.io.BufferedReader;
import java.util.Comparator;

public class StringFileBufferFactory implements IOStackFactory<String> {

    private static final IOStackFactory<? extends Comparable<?>> INSTANCE = new StringFileBufferFactory();

    public static <T extends Comparable<T>> IOStackFactory<T> getInstance() {
        return (IOStackFactory<T>) INSTANCE;
    }

    @Override
    public IOStack<String> create(String fileName, BufferedReader reader, Comparator<String> comparator) {
        return new StringFileBuffer(reader, comparator, fileName);
    }
}
