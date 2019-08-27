package fr.pyjacpp.sudoku.undochange;

import java.util.ArrayList;

import fr.pyjacpp.sudoku.SudokuActivity;
import fr.pyjacpp.sudoku.SudokuGrid;

public class SwapColorUndoChange extends UndoChange {
    private ArrayList<int[]> numberModifies; // list: 0: x, 1: y
    private int startColor;
    private int endColor;

    public SwapColorUndoChange(ArrayList<int[]> numberModifies, int startColor, int endColor) {
        this.numberModifies = numberModifies;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    public UndoChange doUndo(SudokuGrid grid, final SudokuActivity activity) {
        for (int i = 0; i < numberModifies.size(); i++) {
            grid.setColorGrid(numberModifies.get(i)[0], numberModifies.get(i)[1], startColor);
            activity.updateNumberTile(numberModifies.get(i)[0], numberModifies.get(i)[1]);
        }

        return new SwapColorUndoChange(numberModifies, endColor, startColor);
    }
}
