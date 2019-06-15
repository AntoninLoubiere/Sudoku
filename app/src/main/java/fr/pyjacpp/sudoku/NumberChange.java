package fr.pyjacpp.sudoku;

public class NumberChange {

    private int x;
    private int y;
    private int number;

    NumberChange(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumber() {
        return number;
    }
}
