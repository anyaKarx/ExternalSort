package anyakar.externalsort.merge;

import anyakar.externalsort.merge.comparator.ComparatorFactory;
import anyakar.externalsort.merge.params.MergeParams;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MergeSortedFiles<T extends Comparable<T>> {
    MergeParams config;
    private final Comparator<T> comparator;


    public MergeSortedFiles(MergeParams configuration) {
        this.config = configuration;
        this.comparator = ComparatorFactory.getInstance().create(configuration);
    }

    private long merge(BufferedWriter fbw, List<IOStack> buffers) throws IOException {
        PriorityQueue<IOStack> pq = new PriorityQueue<>(new Comparator<IOStack>() {
            @Override
            public int compare(IOStack i, IOStack j) {
                return comparator.compare((T) i.peek(), (T) j.peek());
            }
        });
        for (IOStack bfb : buffers) {
            if (!bfb.empty()) {
                pq.add(bfb);
            }
        }
        long rowcounter = 0;
        try {
            while (pq.size() > 0) {
                IOStack bfb = pq.poll();
                String r = bfb.pop().toString();
                fbw.write(r);
                fbw.newLine();
                ++rowcounter;
                if (bfb.empty()) {
                    bfb.close();
                } else {
                    pq.add(bfb); // add it back
                }
            }
        } finally {
            fbw.close();
            for (IOStack bfb : pq) {
                bfb.close();
            }
        }
        return rowcounter;

    }

    public long merge(List<File> files, File outputfile, Charset cs) throws IOException {
        ArrayList<IOStack> bfbs = new ArrayList<>();
        for (File f : files) {
            final int BUFFER_SIZE = 2048;
            if (f.length() == 0) {
                continue;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), cs));

            IOStack bfb;
            if (config.isString()) {
                bfb = new StringFileBuffer(br, (Comparator<String>) comparator, f.getName());
            } else {
                bfb = new IntegerFileBuffer(br, (Comparator<Integer>) comparator, f.getName());
            }

            bfbs.add(bfb);
        }
        BufferedWriter fbw = new BufferedWriter
                (new OutputStreamWriter
                        (new FileOutputStream(outputfile, true), cs));
        long rowCounter = merge(fbw, bfbs);

        return rowCounter;
    }
}
