package test;

public class Ship {
    private int size;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private int hits;

    public Ship(int size) {
        this.size = size;
        this.hits = 0;
    }

    public int getSize() {
        return size;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public boolean isHit(int row, int col) {
        return row >= startRow && row <= endRow && col >= startCol && col <= endCol;
    }

    public void hit() {
        hits++;
    }

    public boolean isSunk() {
        return hits == size;
    }

    public void setCoordinates(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }
}
