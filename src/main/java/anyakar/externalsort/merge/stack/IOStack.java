package anyakar.externalsort.merge.stack;


import java.io.IOException;

public interface IOStack<T> {
    void close() ;

    boolean empty();

    T peek();

    T pop() throws IOException;

}