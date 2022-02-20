package anyakar.externalsort.merge.stack.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

import anyakar.externalsort.merge.stack.IOStack;

public interface IOStackFactory<T extends Comparable<T>> {

   IOStack<T> create(String fileName, BufferedReader reader, Comparator<T> comparator) throws IOException;
}
