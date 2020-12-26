package fr.pyjacpp.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import java.text.NumberFormat;

import fr.pyjacpp.sudoku.sudoku_grid.SudokuGrid;

public class ConfigureSudokuGridActivity extends AppCompatActivity implements Runnable {

    private EditText editTextSeed;
    private Button validButton;

    private boolean showConflictIsChecked = true; // default value
    private Spinner difficultySpinner;
    private Spinner timerSpinner;
    private SudokuApplication applicationSudoku;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_sudoku_grid);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        applicationSudoku = getApplicationSudoku();

        difficultySpinner = findViewById(R.id.difficultySpinner);
        timerSpinner = findViewById(R.id.timerSpinner);
        editTextSeed = findViewById(R.id.seedEditText);
        validButton = findViewById(R.id.validButton);
        Switch showConflictSwitch = findViewById(R.id.switchShowConflict);

        final int difficulty = applicationSudoku.getLastDifficulty();
        difficultySpinner.setSelection(difficulty);

        timerSpinner.setSelection(applicationSudoku.getLastTimer());

        showConflictSwitch.setChecked(applicationSudoku.getLastConflict());
        showConflictIsChecked = applicationSudoku.getLastConflict();

        ((RadioGroup) findViewById(R.id.sortNotesGroup)).check(
                applicationSudoku.getLastSortUsed() ? R.id.sortNotesSort : R.id.sortNotesAdd
        );

        final NumberFormat numberFormat = NumberFormat.getInstance(getResources().getConfiguration()
                .locale);
        numberFormat.setGroupingUsed(true);

        showConflictSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showConflictIsChecked = isChecked;
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(ConfigureSudokuGridActivity.this).start();
                validButton.setEnabled(false);
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                saveLastOption();
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (editTextSeed.getText().length() > 0)
                    editTextSeed.getText().replace(0, 1, String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextSeed.addTextChangedListener(new TextWatcher() {
            boolean notLock = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (notLock) {
                    notLock = false;
                    int i = 0;
                    while (i < s.length()) {
                        if (s.charAt(i) == '-' || s.charAt(i) == ' ') {
                            s.delete(i, i + 1);
                        } else {
                            i++;
                        }
                    }

                    if (s.length() > SudokuGrid.MAX_LENGTH_SEED + 1) {
                        s.delete(SudokuGrid.MAX_LENGTH_SEED + 1, s.length());
                    }

                    if (s.length() > 0) {
                        char c = s.charAt(0);
                        if ('0' <= c && c <= '4') {
                            difficultySpinner.setSelection(c - '0');
                        } else {
                            s.insert(0, String.valueOf(difficultySpinner.getSelectedItemPosition()));
                        }
                        if (s.length() > 1) {
                            s.insert(1, "-");
                            if (s.length() > 3) {
                                int count = 0;
                                i = s.length() - 1;
                                while (i > 2) {
                                    count++;
                                    if (count >= 3) {
                                        count = 0;
                                        s.insert(i, " ");
                                    }
                                    i--;
                                }
                            }
                        }
                    }
                    notLock = true;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private SudokuApplication getApplicationSudoku() {
        return (SudokuApplication) getApplicationContext();
    }

    private void saveLastOption() {
        applicationSudoku.setDifficulty(
                difficultySpinner.getSelectedItemPosition()
        );

        applicationSudoku.setLastConflict(showConflictIsChecked);

        applicationSudoku.setLastSortUsed(
                ((RadioButton) findViewById(R.id.sortNotesSort)).isChecked()
        );
        applicationSudoku.saveLastOptions();
    }

    @Override
    public void run() {
        Intent sudokuIntent = new Intent(getApplicationContext(), SudokuActivity.class);

        final int difficulty = difficultySpinner.getSelectedItemPosition();
        final boolean sortNotes = ((RadioButton) findViewById(R.id.sortNotesSort)).isChecked();
        final int timerSettings = timerSpinner.getSelectedItemPosition();
        applicationSudoku.setLastTimer(timerSettings);

        long seed;
        boolean randomGrid = true;
        if (editTextSeed.length() > 2) {
            try {
                seed = Long.parseLong(editTextSeed.getText()
                        .toString().substring(2).replace(" ", ""));
                randomGrid = false;
            } catch (NumberFormatException ignored) {
                seed = SudokuGrid.generateRandomSeed();
            }
        } else {
            seed = SudokuGrid.generateRandomSeed();
        }

        ((SudokuApplication) getApplicationContext()).setCurrentSudokuGrid(
                new SudokuGrid(difficulty, timerSettings, seed, sortNotes, randomGrid,
                        showConflictIsChecked)
        );

        startActivity(sudokuIntent);
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

}
