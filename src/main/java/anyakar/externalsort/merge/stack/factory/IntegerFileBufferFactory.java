package anyakar.externalsort.merge.stack.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

import anyakar.externalsort.merge.stack.IOStack;
import anyakar.externalsort.merge.stack.IntegerFileBuffer;

public class IntegerFileBufferFactory  implements IOStackFactory<Integer> {

    private static final IOStackFactory<? extends Comparable<?>> INSTANCE = new IntegerFileBufferFactory();

    public static <T extends Comparable<T>> IOStackFactory<T> getInstance() {
        return (IOStackFactory<T>) INSTANCE;
    }

    @Override
    public IOStack<Integer> create(String fileName, BufferedReader reader, Comparator<Integer> comparator) throws IOException {
        return new IntegerFileBuffer(reader, comparator, fileName);
    }
}
