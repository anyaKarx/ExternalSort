package anyakar.externalsort.merge.stack;

import java.io.IOException;

public interface IOStack<T> {
    public void close() throws IOException;

    public boolean empty();

    public T peek();

    public T pop() throws IOException;

}