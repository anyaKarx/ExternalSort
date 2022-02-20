package anyakar.externalsort.merge.params;


public final class MergeParams {

    private final boolean reverse;
    private final boolean string;

    public MergeParams(boolean reverse, boolean string) {
        this.reverse = reverse;
        this.string = string;
    }

    public boolean isReverse() {
        return reverse;
    }

    public boolean isString() { return string; }
}