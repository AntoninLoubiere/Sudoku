package fr.pyjacpp.sudoku.undochange;

import fr.pyjacpp.sudoku.SudokuActivity;
import fr.pyjacpp.sudoku.SudokuGrid;

public class ColorUndoChange extends UndoChange {
    private int color;
    private int x;
    private int y;

    public ColorUndoChange(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public UndoChange doUndo(SudokuGrid grid, final SudokuActivity activity) {
        ColorUndoChange reverseChange = new ColorUndoChange(x, y, grid.getColorGrid(x, y));
        grid.setColorGrid(x, y, color);

        activity.updateNumberTile(x, y);

        return reverseChange;
    }
}
