package ImageViews;

public enum Comparators {

    DoesEquals {
        public boolean e(int a, int b) { return a == b; }
    },
    DoesntEquals {
        public boolean e(int a, int b) { return a != b; }
    },;

    public abstract boolean e(int a, int b);
}