package fr.pyjacpp.sudoku.undochange;

import android.os.Handler;

import java.util.ArrayList;

import fr.pyjacpp.sudoku.SudokuActivity;
import fr.pyjacpp.sudoku.sudoku_grid.SudokuGrid;
import fr.pyjacpp.sudoku.sudoku_grid.SudokuNumberList;

public class UndoChange {

    private UndoEnum type;

    private SudokuNumberList number; // change number
    private int color; // change number and color change
    private int x; // change number and change color
    private int y; // change number and change color

    private ArrayList<int[]> numberModifies; // list: 0: x, 1: y swap color change
    private int startColor; // swap color change
    private int endColor; // swap color change

    public UndoChange(int x, int y, SudokuNumberList number, int color) {
        this.x = x;
        this.y = y;
        this.number = number.copy();
        this.color = color;

        this.type = UndoEnum.Number;
    }

    public UndoChange(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;

        this.type = UndoEnum.Color;
    }

    public UndoChange(ArrayList<int[]> numberModifies, int startColor, int endColor) {
        this.numberModifies = numberModifies;
        this.startColor = startColor;
        this.endColor = endColor;

        this.type = UndoEnum.SwapColor;
    }

    public UndoChange doUndo(SudokuGrid grid, final SudokuActivity activity) {
        switch (type) {
            case Number:
                if (grid.getUserModify(x, y).getUniqueNumber().isModifiable()) {
                    activity.remainingNumberChange(grid.getUserModify(x, y), number);

                    UndoChange reverseUndo = new UndoChange(
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

            case Color:
                UndoChange reverseChange = new UndoChange(x, y, grid.getColorGrid(x, y));
                grid.setColorGrid(x, y, color);

                activity.updateNumberTile(x, y);

                return reverseChange;

            case SwapColor:
                for (int i = 0; i < numberModifies.size(); i++) {
                    grid.setColorGrid(numberModifies.get(i)[0], numberModifies.get(i)[1], startColor);
                    activity.updateNumberTile(numberModifies.get(i)[0], numberModifies.get(i)[1]);
                }

                return new UndoChange(numberModifies, endColor, startColor);

            default:
                throw new RuntimeException("Undo not implemented");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UndoEnum getType() {
        return type;
    }
}
