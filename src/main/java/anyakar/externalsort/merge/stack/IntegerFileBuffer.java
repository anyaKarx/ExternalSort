package anyakar.externalsort.merge.stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

public final class IntegerFileBuffer implements IOStack {
    private final String fileName;
    private final BufferedReader bufferedReader;
    private Integer cache;
    private final Comparator<Integer> comparator;

    public IntegerFileBuffer(BufferedReader r, Comparator<Integer> cmp, String fileName) throws IOException {
        this.fileName = fileName;
        this.bufferedReader = r;
        this.comparator = cmp;
        String temp = bufferedReader.readLine();
        try {
            this.cache = Integer.parseInt(temp);
        } catch (NumberFormatException exception) {
            System.err.println(exception.getMessage());
            this.cache = null;
            close();
        }
    }

    public void close() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            System.err.printf("Error closing file %s : %s\n", fileName, e.getMessage());
        }
    }

    public boolean empty() {
        return this.cache == null;
    }

    public Integer peek() {
        return this.cache;
    }

    public Integer pop() throws IOException {
        Integer answer = peek();
        reload();
        return answer;
    }

    private void reload() {
        Integer tmp = peek();
        try {
            this.cache = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.printf("Error closing file %s : %s\n", fileName, e.getMessage());
            this.cache = null;
            close();
        }

        if (cache != null && comparator.compare(tmp, this.cache) > 0) {
            System.out.println(this.fileName + " was closed because the data was not sorted");
            this.cache = null;
            close();
        }
    }
}