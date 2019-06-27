package fr.pyjacpp.sudoku;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Float.NaN;

class SudokuStatistics {
    private int numberGameDid = 0;
    private int numberGameWin = 0;
    private int numberNumberCompleted = 0;
    private int numberNumberCompletedJust = 0;
    private int maxNumberCompletedJust = 0;
    private int numberHintAsk = 0;
    private int maxHintAsk = 0;

    private Date resetDate = Calendar.getInstance().getTime();

    void reset() {
        numberGameDid = 0;
        numberGameWin = 0;
        numberNumberCompleted = 0;
        numberNumberCompletedJust = 0;
        maxNumberCompletedJust = 0;
        numberHintAsk = 0;
        maxHintAsk = 0;

        resetDate = Calendar.getInstance().getTime();
    }

    int getNumberGameDid() {
        return numberGameDid;
    }

    int getNumberGameWin() {
        return numberGameWin;
    }

    float getPercentGameWin() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return numberGameWin / (float) numberGameDid * 100;
    }

    int getNumberGameAbort() {
        return numberGameDid - numberGameWin;
    }

    float getPercentGameAbort() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return getNumberGameAbort() / (float) numberGameDid * 100;
    }

    int getNumberNumberCompleted() {
        return numberNumberCompleted;
    }

    float getAverageNumberCompleted() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return numberNumberCompleted / (float) numberGameDid;
    }

    int getNumberNumberCompletedJust() {
        return numberNumberCompletedJust;
    }

    int getNumberNumberCompletedWrong() {
        return numberNumberCompleted - numberNumberCompletedJust;
    }

    float getPercentNumberCompletedWrong() {
        if (numberNumberCompleted == 0) {
            return NaN;
        }
        return getNumberNumberCompletedWrong() / (float) numberNumberCompleted * 100;
    }

    int getMaxNumberCompletedJust() {
        return maxNumberCompletedJust;
    }

    float getPercentNumberCompletedJust() {
        if (numberNumberCompleted == 0) {
            return NaN;
        }
        return numberNumberCompletedJust / (float) numberNumberCompleted * 100;
    }

    int getNumberHintAsk() {
        return numberHintAsk;
    }

    int getMaxHintAsk() {
        return maxHintAsk;
    }

    float getAverageHintAsk() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return numberHintAsk / (float) numberGameDid;
    }

    Date getResetDate() {
        return resetDate;
    }

    void addGame(boolean win) {
        numberGameDid++;
        if (win)
            numberGameWin++;
    }

    void addNumberCompleted(int numberNumberCompleted, int numberNumberCompletedJust) {
        this.numberNumberCompleted += numberNumberCompleted;
        this.numberNumberCompletedJust += numberNumberCompletedJust;

        if (numberNumberCompletedJust > maxNumberCompletedJust)
            maxNumberCompletedJust = numberNumberCompletedJust;
    }

    void addNumberHintAsk(int hintAsk) {
        numberHintAsk += hintAsk;
        if (hintAsk > maxHintAsk)
            maxHintAsk = hintAsk;
    }
}
