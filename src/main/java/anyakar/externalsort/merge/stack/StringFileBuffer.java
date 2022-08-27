package anyakar.externalsort.merge.stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

public final class StringFileBuffer implements IOStack {
    private final String fileName;
    private final BufferedReader bufferedReader;
    private String cache;
    private final Comparator<String> comparator;

    public StringFileBuffer(BufferedReader r, Comparator<String> cmp, String fileName) {
        this.fileName = fileName;
        this.bufferedReader = r;
        this.comparator = cmp;
        try {
            this.cache = this.bufferedReader.readLine();
        } catch (IOException e) {
            System.err.printf("Error while working with file %s : %s\n", fileName, e.getMessage());
        }
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error closing file: %s" + e.getMessage());
        }
    }

    public boolean empty() {
        return this.cache == null;
    }

    public String peek() {
        return this.cache;
    }

    public String pop() {
        if (peek() == null) {
            close();
            return null;
        }
        String answer = peek();// make a copy
        reload();
        return answer;
    }

    private void reload() {
        String tmp = peek();
        try {
            this.cache = this.bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println("Error while working with file %s : %s\n" + fileName + e.getMessage());
        }

        if (this.cache != null) {
            if ((comparator.compare(tmp, this.cache) > 0) || (this.cache.matches("\\s"))) {
                System.err.println(this.fileName + " was closed because the data was not sorted or data was be invalid");
                this.cache = null;
                close();
            }
        }
    }
}
