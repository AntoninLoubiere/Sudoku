package fr.pyjacpp.sudoku;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuNumberList {

    private ArrayList<SudokuNumbersEnum> sudokuNumbersEnumList;
    private SudokuNumbersEnum uniqueNumber;


    SudokuNumberList() {
        sudokuNumbersEnumList = new ArrayList<>();
    }

    SudokuNumberList(SudokuNumbersEnum number) {
        uniqueNumber = number;
    }

    SudokuNumberList(SudokuNumbersEnum[] list) {
        sudokuNumbersEnumList = new ArrayList<>();

        sudokuNumbersEnumList.addAll(Arrays.asList(list));
    }

    public SudokuNumberList(ArrayList<SudokuNumbersEnum> sudokuNumbersEnumList) {
        this.sudokuNumbersEnumList = new ArrayList<>();
        this.sudokuNumbersEnumList.addAll(sudokuNumbersEnumList);
    }

    public SudokuNumbersEnum getUniqueNumber() {
        if (uniqueNumber != null) {
            return uniqueNumber;

        } else if (sudokuNumbersEnumList != null) {
            if (sudokuNumbersEnumList.size() == 0) {
                return SudokuNumbersEnum.Blank;

            } else if (sudokuNumbersEnumList.size() == 1) {

                return sudokuNumbersEnumList.get(0);
            }
        }

        return SudokuNumbersEnum.MultiNumbers;
    }

    SudokuNumbersEnum[] getMultipleNumber() {
        SudokuNumbersEnum[] list;

        if (uniqueNumber != null) {
            list = new SudokuNumbersEnum[1];
            list[0] = uniqueNumber;

        } else if (sudokuNumbersEnumList != null && sudokuNumbersEnumList.size() >= 1) {
            list = new SudokuNumbersEnum[sudokuNumbersEnumList.size()];
            for (int i = 0; i < sudokuNumbersEnumList.size(); i++) {
                list[i] = sudokuNumbersEnumList.get(i);
            }

        } else {
            list = new SudokuNumbersEnum[1];
            list[0] = SudokuNumbersEnum.Blank;
        }

        return list;
    }

    void setUniqueValue(SudokuNumbersEnum sudokuNumbersEnum) {
        uniqueNumber = sudokuNumbersEnum;
    }

    ArrayList<SudokuNumbersEnum> getList() {
        return sudokuNumbersEnumList;
    }

    boolean isUnique() {
        return uniqueNumber != null;
    }

    public SudokuNumberList copy() {
        if (uniqueNumber != null) {
            return new SudokuNumberList(uniqueNumber);
        } else {
            return new SudokuNumberList(sudokuNumbersEnumList);

        }
    }
}
