package fr.pyjacpp.sudoku.sudoku_grid;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

public class Cell {
    public ArrayList<Integer> possibilities;
    public int answer;
    public Cell(int a) {
        if (a <= 0) {
            possibilities = new ArrayList<>();
            possibilities.add(1);
            possibilities.add(2);
            possibilities.add(3);
            possibilities.add(4);
            possibilities.add(5);
            possibilities.add(6);
            possibilities.add(7);
            possibilities.add(8);
            possibilities.add(9);

            answer = 0;
        } else {
            possibilities = new ArrayList<>();
            answer = a;
        }
    }

    @NonNull
    @Override
    public String toString() {
        if (answerKnown()) {
            return "A: " + answer;
        } else {
            return "P";
        }
    }

    public boolean answerKnown() {
        return answer > 0;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
        possibilities.clear();
    }

    public int length() {
        return possibilities.size();
    }

    public void removePossibility(int pos) {
        possibilities.remove((Object) pos);
    }
}
