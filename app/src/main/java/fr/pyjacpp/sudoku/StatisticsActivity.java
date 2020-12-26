package fr.pyjacpp.sudoku;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.pyjacpp.sudoku.statistics.BestGrid;
import fr.pyjacpp.sudoku.statistics.SudokuStatistics;

public class StatisticsActivity extends AppCompatActivity {

    public final LinearLayout.LayoutParams CHILD_PARAMS = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
    public final LinearLayout.LayoutParams LINE_PARAMS = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public StatisticsActivity() {
        CHILD_PARAMS.weight = 1;
    }

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
        LinearLayout bestTimeLayout =
                findViewById(R.id.bestGridLayout);
        LinearLayout bestRandomTimeLayout =
                findViewById(R.id.bestRandomGridLayout);
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


        SudokuStatistics globalStatistics = getGlobalStatistics();
        Resources resources = getResources();
        gameDidTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_game),
                        globalStatistics.getNumberGameDid()));
        gameWinTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_game_win),
                        globalStatistics.getNumberGameWin(),
                        globalStatistics.getPercentGameWin()));
        gameAbortTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_game_abort),
                        globalStatistics.getNumberGameAbort(),
                        globalStatistics.getPercentGameAbort()));
        numberCompletedTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_numbers_completed),
                        globalStatistics.getNumberNumberCompleted()));
        numberCompletedAverageTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_numbers_completed_average),
                        globalStatistics.getAverageNumberCompleted()));
        numberCompletedMaxTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_numbers_completed_max),
                        globalStatistics.getMaxNumberCompletedJust()));
        numberCompletedJustTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_numbers_completed_just),
                        globalStatistics.getNumberNumberCompletedJust(),
                        globalStatistics.getPercentNumberCompletedJust()));
        numberCompletedWrongTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_numbers_completed_wrong),
                        globalStatistics.getNumberNumberCompletedWrong(),
                        globalStatistics.getPercentNumberCompletedWrong()));
        hintTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_hint),
                        globalStatistics.getNumberHintAsk()));
        hintAverageTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_hint_average),
                        globalStatistics.getAverageHintAsk()));
        hintMaxTextView.setText(
                String.format(resources.getString(
                        R.string.statistics_number_hint_max),
                        globalStatistics.getMaxHintAsk()));

        bestTimeLayout.removeAllViews();
        bestRandomTimeLayout.removeAllViews();

        BestGrid[] bestGrids = globalStatistics.getBestGrids();
        BestGrid[] bestRandomGrids = globalStatistics.getBestRandomGrids();
        String[] difficulty = resources.getStringArray(R.array.difficulty);

        String time;
        String seed;

        bestTimeLayout.addView(getTimeLine(
                getString(R.string.difficulty),
                getString(R.string.time),
                getString(R.string.grid_id),
                false), LINE_PARAMS);

        bestRandomTimeLayout.addView(getTimeLine(
                getString(R.string.difficulty),
                getString(R.string.time),
                getString(R.string.grid_id),
                false), LINE_PARAMS);

        NumberFormat numberFormat = NumberFormat.getInstance(getResources().getConfiguration()
                .locale);
        numberFormat.setGroupingUsed(true);

        for (int i = 0; i < bestGrids.length; i++) {
            if (bestGrids[i] != null) {
                time = Utils.getStringFromResolveTime(bestGrids[i].time, true);
                seed = getString(R.string.seed_format, i, numberFormat.format(bestGrids[i].seed));
            } else {
                time = getString(R.string.no_data);
                seed = "";
            }

            LinearLayout timeLine = getTimeLine(
                    difficulty[i],
                    time,
                    seed,
                    bestGrids[i] != null);
            bestTimeLayout.addView(timeLine, LINE_PARAMS);

            if (bestRandomGrids[i] != null) {
                time = Utils.getStringFromResolveTime(bestRandomGrids[i].time, true);
                seed = getString(R.string.seed_format, i, numberFormat.format(bestRandomGrids[i].seed));
            } else {
                time = getString(R.string.no_data);
                seed = "";
            }
            
            bestRandomTimeLayout.addView(getTimeLine(
                    difficulty[i],
                    time,
                    seed,
                    bestRandomGrids[i] != null), LINE_PARAMS);
        }

    }

    @NonNull
    private LinearLayout getTimeLine(String s1, String s2, final String s3, boolean copyLastSeed) {
        LinearLayout line;
        TextView t;
        line = new LinearLayout(this);

        t = new TextView(this);
        t.setTextAppearance(this, R.style.Text_Statistics);
        t.setText(s1);
        line.addView(t, CHILD_PARAMS);

        t = new TextView(this);
        t.setTextAppearance(this, R.style.Text_Statistics);
        t.setText(s2);
        line.addView(t, CHILD_PARAMS);

        t = new TextView(this);
        t.setTextAppearance(this, R.style.Text_Statistics);
        t.setText(s3);

        if (copyLastSeed)
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager)
                            getSystemService(CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("gridId", s3);
                    clipboardManager.setPrimaryClip(data);

                    Toast.makeText(StatisticsActivity.this, getString(R.string.id_grid_copy),
                            Toast.LENGTH_LONG).show();
                }
            });

        line.addView(t, CHILD_PARAMS);
        return line;
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
