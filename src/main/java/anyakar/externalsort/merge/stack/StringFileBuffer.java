package anyakar.externalsort.merge.stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

public final class StringFileBuffer implements IOStack {

    public StringFileBuffer(BufferedReader r, Comparator<String> cmp, String fileName) throws IOException {
        this.fileName = fileName;
        this.fbr = r;
        this.cmp = cmp;
        this.cache = this.fbr.readLine();
    }

    public void close() throws IOException { this.fbr.close(); }

    public boolean empty() {
        return this.cache == null;
    }

    public String peek() {
        return this.cache;
    }

    public String pop() throws IOException {
        String answer = peek().toString();// make a copy
        reload();
        return answer;
    }

    private void reload() throws IOException {
        String tmp = peek().toString();

        this.cache = this.fbr.readLine();
        if (this.cache != null) {
            if ((cmp.compare(tmp, this.cache) > 0) || (this.cache.matches("\\s"))) {
                System.out.println(this.fileName + " was closed because the data was not sorted");
                this.cache = null;
            }
        }
    }
    private String fileName;
    private BufferedReader fbr;
    private String cache;
    private Comparator<String> cmp;
}
