package fr.pyjacpp.sudoku;

import android.app.Application;

public class SudokuApplication extends Application {
    private SudokuGrid currentSudokuGrid = null;

    public SudokuGrid getCurrentSudokuGrid() {
        return currentSudokuGrid;
    }

    public void setCurrentSudokuGrid(SudokuGrid currentSudokuGrid) {
        this.currentSudokuGrid = currentSudokuGrid;
    }
}
