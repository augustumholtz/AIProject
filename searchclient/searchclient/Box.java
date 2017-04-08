package searchclient;

public class Goal {
    char letter;
    int row, col;
    public Goal(char l, int r, int c) {
        letter = l;
        row = r;
        col = c;
    }

    public char getLetter() {
        return letter;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
