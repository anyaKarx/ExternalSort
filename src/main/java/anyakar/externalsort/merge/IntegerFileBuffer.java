package anyakar.externalsort.merge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

public final class IntegerFileBuffer implements IOStack {

    public IntegerFileBuffer(BufferedReader r, Comparator<Integer> cmp, String fileName) throws IOException {
        this.fileName = fileName;
        this.fbr = r;
        this.cmp = cmp;
        this.cache = Integer.parseInt(String.valueOf(fbr.readLine()));
    }

    public void close() throws IOException { this.fbr.close(); }

    public boolean empty() {
        return this.cache == null;
    }

    public Integer peek() {
        return this.cache;
    }

    public Integer pop() throws IOException {
        Integer answer = peek();// make a copy
        reload();
        return answer;
    }

    private void reload() throws IOException {
        Integer tmp = peek();
        String i = fbr.readLine();
        if (i == null) this.cache = null;
        else {
            this.cache = Integer.parseInt(String.valueOf(i));
            if (cmp.compare(tmp, this.cache) > 0) {
                System.out.println(this.fileName + " was closed because the data was not sorted");
                this.cache = null;
            }
        }

    }

    private String fileName;
    private BufferedReader fbr;
    private Integer cache;
    private Comparator<Integer> cmp;
}