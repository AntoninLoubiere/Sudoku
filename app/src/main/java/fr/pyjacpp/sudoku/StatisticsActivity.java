package fr.pyjacpp.sudoku;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

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

        Button resetButton = findViewById(R.id.reinitButton);
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
        String timeSpan = DateUtils.getRelativeTimeSpanString(
                getGlobalStatistics().getResetDate().getTime(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS).toString().toLowerCase();

        resetTextView.setText(
                String.format(getResources().getString(
                        R.string.statistics_reset_date),
                        dateFormat.format(getGlobalStatistics().getResetDate()),
                        timeSpan));
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
