package fr.pyjacpp.sudoku;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTextStatistics();

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(StatisticsActivity.this)
                        .setTitle(R.string.dialog_reset_statistics_title)
                        .setMessage(R.string.dialog_reset_statistics_message)
                        .setPositiveButton(R.string.dialog_valid, new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getGlobalStatistics().reset();
                                        ((SudokuApplication) getApplicationContext()).
                                                saveStatistics();
                                        setTextStatistics();
                                        setResetDateText();
                                    }
                                })
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .show();
            }
        });
        new GregorianCalendar().getTime();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setResetDateText();
    }

    private void setResetDateText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy",
                getResources().getConfiguration().locale);

        TextView resetTextView =
                findViewById(R.id.reinitTextView);

        String timeSpent = getTimeSpent(getGlobalStatistics().getResetDate());

        resetTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_reset_date),
                        dateFormat.format(getGlobalStatistics().getResetDate()),
                        timeSpent));
    }

    private String getTimeSpent(Date resetDate) {
        long timeDifferenceInMillis = System.currentTimeMillis() - resetDate.getTime();

        if (timeDifferenceInMillis < 1000 * 60) {
            return getResources().getString(R.string.time_spent_now);

        } else if (timeDifferenceInMillis < 1000 * 60 * 60) {
            long numberMinutes = timeDifferenceInMillis / (1000 * 60);

            if (numberMinutes <= 1) {
                return getResources().getString(R.string.time_spent_minute_single);
            } else {
                return String.format(getResources().getString(R.string.time_spent_minute_multiple),
                        numberMinutes);
            }

        } else if (timeDifferenceInMillis < 1000 * 60 * 60 * 24) {
            long numberHours = timeDifferenceInMillis / (1000 * 60 * 60);

            if (numberHours <= 1) {
                return getResources().getString(R.string.time_spent_hour_single);
            } else {
                return String.format(getResources().getString(R.string.time_spent_hour_multiple),
                        numberHours);
            }

        } else if (timeDifferenceInMillis < 1000 * 60 * 60 * 24 * 7) {
            long numberDay = timeDifferenceInMillis / (1000 * 60 * 60 * 24);

            if (numberDay <= 1) {
                return getResources().getString(R.string.time_spent_day_single);
            } else {
                return String.format(getResources().getString(R.string.time_spent_day_multiple),
                        numberDay);
            }
        } else if (timeDifferenceInMillis < 1000 * 60 * 60 * 24 * (365 / 12f)) {
            long numberWeek = timeDifferenceInMillis / (1000 * 60 * 60 * 24 * 7);

            if (numberWeek <= 1) {
                return getResources().getString(R.string.time_spent_week_single);
            } else {
                return String.format(getResources().getString(R.string.time_spent_week_multiple),
                        numberWeek);
            }
        } else if (timeDifferenceInMillis < 1000 * 60 * 60 * 24 * 365.25f) {
            long numberMonth = (long) (timeDifferenceInMillis / (1000 * 60 * 60 * 24 * (365 / 12f)));

            if (numberMonth <= 1) {
                return getResources().getString(R.string.time_spent_month_single);
            } else {
                return String.format(getResources().getString(R.string.time_spent_month_multiple),
                        numberMonth);
            }
        } else {
            long numberYear = (long) (timeDifferenceInMillis / (1000 * 60 * 60 * 24 * 365.25f));

            if (numberYear <= 1) {
                return getResources().getString(R.string.time_spent_year_single);
            } else {
                return String.format(getResources().getString(R.string.time_spent_year_multiple),
                        numberYear);
            }
        }
    }

    private void setTextStatistics() {
        TextView gameDidTextView =
                findViewById(R.id.numberGameDidTextView);
        TextView gameWinTextView =
                findViewById(R.id.numberGameWinTextView);
        TextView gameAbortTextView =
                findViewById(R.id.numberGameAbortTextView);
        TextView numberCompletedTextView =
                findViewById(R.id.numberNumbersCompletedTextView);
        TextView numberCompletedAverageTextView =
                findViewById(R.id.numberNumbersCompletedAverageTextView);
        TextView numberCompletedMaxTextView =
                findViewById(R.id.numberNumbersCompletedMaxTextView);
        TextView numberCompletedJustTextView =
                findViewById(R.id.numberNumbersCompletedJustTextView);
        TextView numberCompletedWrongTextView =
                findViewById(R.id.numberNumbersCompletedWrongTextView);
        TextView hintTextView =
                findViewById(R.id.numberHintTextView);
        TextView hintAverageTextView =
                findViewById(R.id.numberHintAverageTextView);
        TextView hintMaxTextView =
                findViewById(R.id.numberHintMaxTextView);


        gameDidTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_game),
                        getGlobalStatistics().getNumberGameDid()));
        gameWinTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_game_win),
                        getGlobalStatistics().getNumberGameWin(),
                        getGlobalStatistics().getPercentGameWin()));
        gameAbortTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_game_abort),
                        getGlobalStatistics().getNumberGameAbort(),
                        getGlobalStatistics().getPercentGameAbort()));
        numberCompletedTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_numbers_completed),
                        getGlobalStatistics().getNumberNumberCompleted()));
        numberCompletedAverageTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_numbers_completed_average),
                        getGlobalStatistics().getAverageNumberCompleted()));
        numberCompletedMaxTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_numbers_completed_max),
                        getGlobalStatistics().getMaxNumberCompletedJust()));
        numberCompletedJustTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_numbers_completed_just),
                        getGlobalStatistics().getNumberNumberCompletedJust(),
                        getGlobalStatistics().getPercentNumberCompletedJust()));
        numberCompletedWrongTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_numbers_completed_wrong),
                        getGlobalStatistics().getNumberNumberCompletedWrong(),
                        getGlobalStatistics().getPercentNumberCompletedWrong()));
        hintTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_hint),
                        getGlobalStatistics().getNumberHintAsk()));
        hintAverageTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_hint_average),
                        getGlobalStatistics().getAverageHintAsk()));
        hintMaxTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_number_hint_max),
                        getGlobalStatistics().getMaxHintAsk()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private SudokuStatistics getGlobalStatistics() {
        return ((SudokuApplication) getApplicationContext()).getSudokuGlobalStatistics();
    }
}
