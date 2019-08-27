package fr.pyjacpp.sudoku.undochange;

import android.os.Handler;

import fr.pyjacpp.sudoku.SudokuActivity;
import fr.pyjacpp.sudoku.SudokuGrid;
import fr.pyjacpp.sudoku.SudokuNumbersEnum;

public class NumberUndoChange extends UndoChange{

    private SudokuNumbersEnum number;
    private int color;
    private int x;
    private int y;

    public NumberUndoChange(int x, int y, SudokuNumbersEnum number, int color) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public UndoChange doUndo(SudokuGrid grid, final SudokuActivity activity) {
        if (grid.getUserModify(x, y).isModifiable()) {
            NumberUndoChange reverseUndo = new NumberUndoChange(
                    x, y, grid.getUserModify(x, y), grid.getColorGrid(x, y)
            );
            grid.setUserModify(x, y, number);
            grid.setColorGrid(x, y, color);

            activity.updateNumberTile(x, y);

            // remove / add conflicts
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    activity.testConflict(x, y);
                }
            });
            return reverseUndo;
        }
        return null;
    }
}
