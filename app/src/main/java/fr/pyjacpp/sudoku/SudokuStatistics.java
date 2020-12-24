package fr.pyjacpp.sudoku;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Float.NaN;

public class SudokuStatistics {
    private int numberGameDid = 0;
    private int numberGameWin = 0;
    private int numberNumberCompleted = 0;
    private int numberNumberCompletedJust = 0;
    private int maxNumberCompletedJust = 0;
    private int numberHintAsk = 0;
    private int maxHintAsk = 0;

    private Date resetDate = Calendar.getInstance().getTime();

    public void reset() {
        numberGameDid = 0;
        numberGameWin = 0;
        numberNumberCompleted = 0;
        numberNumberCompletedJust = 0;
        maxNumberCompletedJust = 0;
        numberHintAsk = 0;
        maxHintAsk = 0;

        resetDate = Calendar.getInstance().getTime();
    }

    public int getNumberGameDid() {
        return numberGameDid;
    }

    public int getNumberGameWin() {
        return numberGameWin;
    }

    public float getPercentGameWin() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return numberGameWin / (float) numberGameDid * 100;
    }

    public int getNumberGameAbort() {
        return numberGameDid - numberGameWin;
    }

    public float getPercentGameAbort() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return getNumberGameAbort() / (float) numberGameDid * 100;
    }

    public int getNumberNumberCompleted() {
        return numberNumberCompleted;
    }

    public float getAverageNumberCompleted() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return numberNumberCompleted / (float) numberGameDid;
    }

    public int getNumberNumberCompletedJust() {
        return numberNumberCompletedJust;
    }

    public int getNumberNumberCompletedWrong() {
        return numberNumberCompleted - numberNumberCompletedJust;
    }

    public float getPercentNumberCompletedWrong() {
        if (numberNumberCompleted == 0) {
            return NaN;
        }
        return getNumberNumberCompletedWrong() / (float) numberNumberCompleted * 100;
    }

    public int getMaxNumberCompletedJust() {
        return maxNumberCompletedJust;
    }

    public float getPercentNumberCompletedJust() {
        if (numberNumberCompleted == 0) {
            return NaN;
        }
        return numberNumberCompletedJust / (float) numberNumberCompleted * 100;
    }

    public int getNumberHintAsk() {
        return numberHintAsk;
    }

    public int getMaxHintAsk() {
        return maxHintAsk;
    }

    public float getAverageHintAsk() {
        if (numberGameDid == 0) {
            return NaN;
        }
        return numberHintAsk / (float) numberGameDid;
    }

    public Date getResetDate() {
        return resetDate;
    }

    public void addGame(boolean win) {
        numberGameDid++;
        if (win)
            numberGameWin++;
    }

    public void addNumberCompleted(int numberNumberCompleted, int numberNumberCompletedJust) {
        this.numberNumberCompleted += numberNumberCompleted;
        this.numberNumberCompletedJust += numberNumberCompletedJust;

        if (numberNumberCompletedJust > maxNumberCompletedJust)
            maxNumberCompletedJust = numberNumberCompletedJust;
    }

    public void addNumberHintAsk(int hintAsk) {
        numberHintAsk += hintAsk;
        if (hintAsk > maxHintAsk)
            maxHintAsk = hintAsk;
    }
}
