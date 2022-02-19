package anyakar.externalsort.merge;

import anyakar.externalsort.merge.params.MergeParams;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import anyakar.externalsort.merge.comparator.ComparatorFactory;

public class MergeSortedFiles<T extends Comparable<T>> {
    MergeParams config;
    //private final Serializer<T> serializer;
    private final Comparator<T> comparator;

    public static interface Serializer<T> {

        void writeValues(Iterator<T> values, OutputStream out) throws IOException;

        Iterator<T> readValues(InputStream input) throws IOException;

    }
    public MergeSortedFiles( MergeParams configuration)
    {
        this.config = configuration;
        this.comparator = ComparatorFactory.getInstance().create(configuration);
        //this.serializer = srz;
    }

    private final class StringFileBuffer implements IOStack {
        public StringFileBuffer(BufferedReader r, Comparator<String> cmp) throws IOException {
            this.fbr = r;
            this.cmp = cmp;
            reload();
        }
        public void close() throws IOException {
            this.fbr.close();
        }

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
            if (cmp.compare(tmp, this.cache) < 0) {
                this.close();
            } else if (this.cache.matches("\\s")) {
                this.close();
            }
        }

        private BufferedReader fbr;
        private String cache;
        private Comparator<String> cmp;
    }

    private final class IntegerFileBuffer implements IOStack {
        public IntegerFileBuffer(BufferedReader r, Comparator<Integer> cmp) throws IOException {
            this.fbr = r;
            this.cmp = cmp;
            reload();
        }
        public void close() throws IOException {
            this.fbr.close();
        }

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

            this.cache = Integer.getInteger(this.fbr.readLine());
            if (cmp.compare(tmp, this.cache) < 0) {
                this.close();
            }
        }

        private BufferedReader fbr;
        private Integer cache;
        private Comparator<Integer> cmp;
    }

    private long merge(BufferedWriter fbw,
                        List<IOStack> buffers) throws IOException
    {
        PriorityQueue<IOStack> pq = new PriorityQueue<>( new Comparator<IOStack>() {
            @Override
            public int compare(IOStack i,
                               IOStack j) {
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

    public  long merge(List<File> files, File outputfile) throws IOException
    {
        ArrayList<IOStack> bfbs = new ArrayList<>();
        for (File f : files) {
            final int BUFFERSIZE = 2048;
            if (f.length() == 0) {
                continue;
            }
            InputStream in = new FileInputStream(f);
            BufferedReader br;

                br = new BufferedReader(new InputStreamReader(
                        in));

                IOStack bfb;
            if (config.isString()){
                 bfb = new StringFileBuffer(br, (Comparator<String>) comparator);
            }else{
                 bfb = new IntegerFileBuffer(br, (Comparator<Integer>) comparator);
            }

            bfbs.add(bfb);
        }
        BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputfile, true)));
        long rowcounter = merge(fbw, bfbs);
        for (File f : files) {
            f.delete();
        }
        return rowcounter;
    }
}
