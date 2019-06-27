package fr.pyjacpp.sudoku;

public enum SudokuNumbersEnum {

    Blank("", 0),

    One("1", 1),
    Two("2", 2),
    Three("3", 3),
    Four("4", 4),
    Five("5", 5),
    Six("6", 6),
    Seven("7", 7),
    Eight("8", 8),
    Nine("9", 9),

    Square("■", 10),
    Round("●", 11),


    NotModifiable("", -1),
    Hint("", -1);

    private String textNumber;
    private int number;

    SudokuNumbersEnum(String textNumber, int number) {
        this.textNumber = textNumber;
        this.number = number;
    }

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
            case 10:
                return Square;
            case 11:
                return Round;

            default:
                return Blank;
        }
    }

    public String getTextNumber() {
        return textNumber;
    }

    public int getNumber() {
        return number;
    }

    public boolean isNumber() {
        switch (number) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return true;

            default:
                return false;
        }
    }

    public boolean isModifiable() {
        return number != -1;
    }
}
