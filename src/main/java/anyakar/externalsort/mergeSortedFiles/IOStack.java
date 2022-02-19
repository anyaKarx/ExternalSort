package anyakar.externalsort.mergeSortedFiles;

import java.io.IOException;

/**
 * General interface to abstract away BinaryFileBuffer
 * so that users of the library can roll their own.
 */
public interface IOStack<T> {
    public void close() throws IOException;

    public boolean empty();

    public T peek();

    public T pop() throws IOException;

}