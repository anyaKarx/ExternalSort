package anyakar.externalsort.merge.comparator;

import java.util.Comparator;

import anyakar.externalsort.merge.params.MergeParams;

public class ComparatorFactory {

    private static final ComparatorFactory INSTANCE = new ComparatorFactory();

    private ComparatorFactory() {
    }

    public static ComparatorFactory getInstance() {
        return INSTANCE;
    }

    public <T extends Comparable<T>> Comparator<T> create(MergeParams mergeParams) {
        return Comparable::compareTo;
    }
}
