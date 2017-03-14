package searchclient;
public class Pair {
    public int r;
    public int c;
    public Pair(int row, int col) {
        r = row;
        c = col;
    }

    @Override
    public int hashCode() {
        return (r*2 + c*3);
    }

    @Override
    public boolean equals(Object obj) {
        Pair pair = (Pair) obj;
        return this.r == pair.r && this.c == pair.c;
    }
}