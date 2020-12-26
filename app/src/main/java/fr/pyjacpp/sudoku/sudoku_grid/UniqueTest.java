package fr.pyjacpp.sudoku.sudoku_grid;

import android.util.Log;

import java.util.ArrayList;

public class UniqueTest {

    private final Cell[][] cells = new Cell[9][9];
    private boolean solutionFound = false;
    private boolean breaker = false;
    private final ArrayList<Integer> emptyArrayList = new ArrayList<>();

    public UniqueTest(SudokuNumbersEnum[][] grid, SudokuNumberList[][] userModify) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuNumbersEnum uniqueNumber = userModify[x][y].getUniqueNumber();
                if (uniqueNumber.isModifiable()) {
                    cells[x][y] = new Cell(0);
                } else {
                    cells[x][y] = new Cell(grid[x][y].getNumber());
                }
            }
        }
        encodeGrid();
    }

    private void encodeGrid() {
        Cell cell;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                cell = cells[x][y];
                if (cell.answerKnown()) {
                    removePosition(x, y, cell.answer);
                }
            }
        }
    }

    private boolean removePossibilityAlone() {
        ArrayList<Integer[]> to_update = new ArrayList<>();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (cells[x][y].length() == 1) {
                    to_update.add(new Integer[]{x, y});
                }
            }
        }

        if (to_update.isEmpty()) {
            return false;
        } else {
            int x;
            int y;
            int ans;
            Cell cell;
            for (Integer[] t : to_update) {
                x = t[0];
                y = t[1];
                cell = cells[x][y];
                ans = cell.possibilities.get(0);
                cell.setAnswer(ans);
                removePosition(x, y, cell.answer);
            }
            return true;
        }
    }

    private void removePosition(int x, int y, int answer) {
        Cell cell;
        for (int cx = 0; cx < 9; cx++) {
            cell = cells[cx][y];
            if (cell.possibilities.contains(answer)) {
                cell.removePossibility(answer);
            }
        }

        for (int cy = 0; cy < 9; cy++) {
            cell = cells[x][cy];
            if (cell.possibilities.contains(answer)) {
                cell.removePossibility(answer);
            }
        }

        int tx = x - x % 3 + 3;
        int ty = y - y % 3 + 3;
        for (int cx = tx - 3; cx < tx; cx++) {
            for (int cy = ty - 3; cy < ty; cy++) {
                cell = cells[cx][cy];
                if (cell.possibilities.contains(answer)) {
                    cell.removePossibility(answer);
                }
            }
        }
    }

    private ArrayList<Integer[]> removePositionChange(int x, int y, int answer) {
        ArrayList<Integer[]> changes = new ArrayList<>();
        Cell cell;
        for (int cx = 0; cx < 9; cx++) {
            cell = cells[cx][y];
            if (cell.possibilities.contains(answer)) {
                cell.removePossibility(answer);
                changes.add(new Integer[]{cx, y});
            }
        }

        for (int cy = 0; cy < 9; cy++) {
            cell = cells[x][cy];
            if (cell.possibilities.contains(answer)) {
                cell.removePossibility(answer);
                changes.add(new Integer[]{x, cy});
            }
        }

        int tx = x - x % 3 + 3;
        int ty = y - y % 3 + 3;
        for (int cx = tx - 3; cx < tx; cx++) {
            for (int cy = ty - 3; cy < ty; cy++) {
                cell = cells[cx][cy];
                if (cell.possibilities.contains(answer)) {
                    cell.removePossibility(answer);
                    changes.add(new Integer[]{cx, cy});
                }
            }
        }
        return changes;
    }

    public boolean hasUniqueSolution() {
        uniqueSolutionRecursive();
        return solutionFound;
    }

    private void uniqueSolutionRecursive() {
        int[] minPos = {-1, -1};
        int minValue = 10;

        Cell cell;
        int length;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                cell = cells[x][y];
                length = cell.length();
                if (!cell.answerKnown() && length < minValue) {
                    if (length <= 0) return;

                    minPos[0] = x;
                    minPos[1] = y;
                    minValue = length;
                }
            }
        }
        if (minPos[0] < 0) {
            if (solutionFound) {
                solutionFound = false;
                breaker = true;
            } else {
                solutionFound = true;
            }
        } else {
            cell = cells[minPos[0]][minPos[1]];
            ArrayList<Integer> possibilities = cell.possibilities;
            cell.possibilities = emptyArrayList;
            for (Integer n : possibilities) {
                if (canBePlace(minPos[0], minPos[1], n)) {
                    cell.setAnswer(n);

                    ArrayList<Integer[]> changes = removePositionChange(minPos[0], minPos[1], n);
                    uniqueSolutionRecursive();

                    if (breaker) return;

                    cleanChangeList(changes, n);
                }
            }
            cell.answer = 0;
            cell.possibilities = possibilities;
        }
    }

    private void cleanChangeList(ArrayList<Integer[]> changes, int answer) {
        for (Integer[] i: changes) {
            cells[i[0]][i[1]].possibilities.add(answer);
        }
    }

    private boolean canBePlace(int x, int y, int answer) {
        Cell cell;
        for (int cx = 0; cx < 9; cx++) {
            cell = cells[cx][y];
            if (cell.answer == answer) return false;
        }

        for (int cy = 0; cy < 9; cy++) {
            cell = cells[x][cy];
            if (cell.answer == answer) return false;
        }

        int tx = x - x % 3 + 3;
        int ty = y - y % 3 + 3;
        for (int cx = tx - 3; cx < tx; cx++) {
            for (int cy = ty - 3; cy < ty; cy++) {
                cell = cells[cx][cy];
                if (cell.answer == answer) return false;
            }
        }

        return true;
    }
}

