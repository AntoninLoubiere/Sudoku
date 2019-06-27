package fr.pyjacpp.sudoku;

class NumberChange {

    private int x;
    private int y;
    private SudokuNumbersEnum number;

    NumberChange(int x, int y, SudokuNumbersEnum number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    SudokuNumbersEnum getNumber() {
        return number;
    }
}
