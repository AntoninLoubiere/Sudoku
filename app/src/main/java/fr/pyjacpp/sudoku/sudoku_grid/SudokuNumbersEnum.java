package fr.pyjacpp.sudoku.sudoku_grid;

import android.support.annotation.NonNull;

public enum SudokuNumbersEnum {

    Blank,

    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,


    NotModifiable,
    Hint,
    MultiNumbers;

    public static SudokuNumbersEnum get(int number) {
        switch (number) {
            case 1:
                return One;
            case 2:
                return Two;
            case 3:
                return Three;
            case 4:
                return Four;
            case 5:
                return Five;
            case 6:
                return Six;
            case 7:
                return Seven;
            case 8:
                return Eight;
            case 9:
                return Nine;

            default:
                return Blank;
        }
    }

    public String getTextNumber() {
        switch (this) {
            case One:
                return "1";

            case Two:
                return "2";

            case Three:
                return "3";

            case Four:
                return "4";

            case Five:
                return "5";

            case Six:
                return "6";

            case Seven:
                return "7";

            case Eight:
                return "8";

            case Nine:
                return "9";
            default:
                return "";
        }
    }

    public int getNumber() {
        switch (this) {
            case One:
                return 1;

            case Two:
                return 2;

            case Three:
                return 3;

            case Four:
                return 4;

            case Five:
                return 5;

            case Six:
                return 6;

            case Seven:
                return 7;

            case Eight:
                return 8;

            case Nine:
                return 9;
            default:
                return -1;
        }
    }

    public boolean isNumber() {
        switch (this) {
            case One:
            case Two:
            case Three:
            case Four:
            case Five:
            case Six:
            case Seven:
            case Eight:
            case Nine:
                return true;

            default:
                return false;
        }
    }

    public boolean isModifiable() {
        switch (this) {
            case NotModifiable:
            case Hint:
                return false;

            default:
                return true;
        }
    }

    public boolean canBeConflict() {
        switch (this) {
            case One:
            case Two:
            case Three:
            case Four:
            case Five:
            case Six:
            case Seven:
            case Eight:
            case Nine:
                return true;

            default:
                return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getTextNumber();
    }
}
