package anyakar.externalsort.merge;
import anyakar.externalsort.merge.stack.IOStack;
import anyakar.externalsort.merge.stack.factory.IOStackFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MergeSortedFiles<T extends Comparable<T>> {

    private final Comparator<T> comparator;
    private final IOStackFactory<T> ioStackFactory;


    public MergeSortedFiles(Comparator<T> comparator, IOStackFactory<T> ioStackFactory) {
        this.comparator = comparator;
        this.ioStackFactory = ioStackFactory;
    }

    private long merge(BufferedWriter fbw, List<IOStack<T>> buffers) throws IOException {
        PriorityQueue<IOStack<T>> pq = new PriorityQueue<>((i, j) -> comparator.compare(i.peek(), j.peek()));
        for (IOStack<T> bfb : buffers) {
            if (!bfb.empty()) {
                pq.add(bfb);
            }
        }
        long rowCounter = 0;
        try {
            while (pq.size() > 0) {
                IOStack<T> bfb = pq.poll();
                String r = bfb.pop().toString();
                fbw.write(r);
                fbw.newLine();
                ++rowCounter;
                if (bfb.empty()) {
                    bfb.close();
                } else {
                    pq.add(bfb); // add it back
                }
            }
        } finally {
            fbw.close();
            for (IOStack<T> bfb : pq) {
                bfb.close();
            }
        }
        return rowCounter;

    }

    public long merge(List<File> files, File outputfile, Charset cs) throws IOException {
        List<IOStack<T>> bfbs = new ArrayList<>();
        for (File f : files) {
            if (f.length() == 0) {
                continue;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), cs));
            IOStack<T> bfb = ioStackFactory.create(f.getName(), br, comparator);
            bfbs.add(bfb);
        }
        BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputfile, true), cs));

        return merge(fbw, bfbs);
    }
}

