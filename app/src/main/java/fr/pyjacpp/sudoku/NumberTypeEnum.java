package fr.pyjacpp.sudoku;

public enum NumberTypeEnum {
    Number, UserInput, Hint, Win, Lose;

    public boolean isModifiable() {
        switch (this) {
            case Number:
            case Hint:
            case Win:
            case Lose:
            default:
                return false;

            case UserInput:
                return true;
        }
    }
}
