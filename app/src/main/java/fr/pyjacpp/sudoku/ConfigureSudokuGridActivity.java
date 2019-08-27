package fr.pyjacpp.sudoku;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import java.text.NumberFormat;

public class ConfigureSudokuGridActivity extends AppCompatActivity {

    SeekBar seekBarNumberTileToRemove;
    EditText editTextNumberTileToRemove;
    EditText editTextSeed;
    Button validButton;
    Switch showConflictSwitch;
    boolean showConflictIsChecked = true; // default value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int numberTileToRemove = getIntent().getIntExtra("numberTilesToRemove",
                40);

        boolean showConflict = getIntent().getBooleanExtra("showConflictSwitch",
                true);

        setContentView(R.layout.activity_configure_sudoku_grid);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        seekBarNumberTileToRemove = findViewById(R.id.seekBarNumberTileToRemove);
        editTextNumberTileToRemove = findViewById(R.id.editTextNumberTileToRemove);
        editTextSeed = findViewById(R.id.seedEditText);
        validButton = findViewById(R.id.validButton);
        showConflictSwitch = findViewById(R.id.switchShowConflict);

        updateNumberToRemove(numberTileToRemove);
        editTextNumberTileToRemove.setText(String.valueOf(numberTileToRemove));
        showConflictSwitch.setChecked(showConflict);
        showConflictIsChecked = showConflict;

        seekBarNumberTileToRemove.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    editTextNumberTileToRemove.setText(String.valueOf(progress + 20));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        editTextNumberTileToRemove.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int input = Integer.parseInt(String.valueOf(s));

                    updateNumberToRemove(input, false);
                } catch (NumberFormatException ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final NumberFormat numberFormat = NumberFormat.getInstance(getResources().getConfiguration()
                .locale);
        numberFormat.setGroupingUsed(true);

        editTextSeed.addTextChangedListener(new TextWatcher() {
            int cursorPosition;
            StringBuilder text;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                cursorPosition = editTextSeed.getSelectionEnd();
                text = new StringBuilder(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextSeed.removeTextChangedListener(this);
                if (text.length() >= 1 && text.charAt(0) == '-') {
                    text.deleteCharAt(0);
                    cursorPosition = 0;
                }
                if (text.length() >= 2 && text.charAt(1) == '-') {
                    text.insert(0, '0');
                    cursorPosition++;
                }
                if (text.length() >= 4 && text.charAt(3) == '-') {
                    text.deleteCharAt(2);
                    if (cursorPosition >= 4)
                        cursorPosition--;
                } else if (text.length() >= 3 && text.charAt(2) != '-') {
                    if (cursorPosition >= 3)
                        cursorPosition++;
                    text.insert(2, '-');
                }
                if (text.length() >= 4) {
                    int i = 3;
                    while (i < text.length()) {
                        if (text.charAt(i) =='-' || text.charAt(i) == ' ') {
                            text.deleteCharAt(i);
                            if (cursorPosition > i)
                                cursorPosition--;
                            continue;
                        }
                        i++;
                    }

                    i = text.length() - 1;

                    int numberSeparatorCount = 0;
                    while (i > 3) {
                        numberSeparatorCount++;
                        if (numberSeparatorCount >= 3) {
                            text.insert(i, ' ');
                            numberSeparatorCount = 0;
                            if (cursorPosition > i)
                                cursorPosition++;
                        }
                        i--;
                    }
                }

                if (text.length() >= 2) {
                    try {
                        updateNumberToRemove(Integer.parseInt(text.substring(0, 2)));
                    } catch (NumberFormatException ignored) {
                    }
                }

                editTextSeed.setText(text.toString());
                if (cursorPosition > text.length()) {
                    cursorPosition = text.length();
                }
                editTextSeed.setSelection(cursorPosition);
                editTextSeed.addTextChangedListener(this);
            }
        });

        showConflictSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showConflictIsChecked = isChecked;
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sudokuIntent = new Intent(getApplicationContext(), SudokuActivity.class);
                sudokuIntent.putExtra("numberTilesToRemove",
                        Integer.parseInt(editTextNumberTileToRemove.getText().toString()));
                sudokuIntent.putExtra("showConflictSwitch", showConflictIsChecked);
                if (editTextSeed.length() > 3) {
                    try {
                        sudokuIntent.putExtra("seed", Long.parseLong(editTextSeed.getText()
                                .toString().substring(3).replace(" ", "")));
                    } catch (NumberFormatException ignored) {
                    }
                }

                ((SudokuApplication) getApplicationContext()).setCurrentSudokuGrid(null);

                startActivity(sudokuIntent);
                finish();
            }
        });
    }

    private void updateNumberToRemove(int value) {
        updateNumberToRemove(value, true);
    }

    @SuppressLint("SetTextI18n")
    private void updateNumberToRemove(int value, boolean updateEntry) {
        if (updateEntry) {
            int cursorPosition = editTextNumberTileToRemove.getSelectionEnd();
            editTextNumberTileToRemove.setText(String.valueOf(value));
            if (cursorPosition > editTextNumberTileToRemove.length()) {
                cursorPosition = editTextNumberTileToRemove.length();
            }
            editTextNumberTileToRemove.setSelection(cursorPosition);
        } else if (editTextSeed.getText().length() <= 2) {
            editTextSeed.setText(String.valueOf(value) + '-');
        }

        if (editTextSeed.getText().length() >= 2) {
            int cursorPosition = editTextSeed.getSelectionEnd();
            editTextSeed.setText(String.valueOf(value) +
                    editTextSeed.getText().subSequence(2, editTextSeed.length()));
            if (cursorPosition > editTextNumberTileToRemove.length()) {
                cursorPosition = editTextNumberTileToRemove.length();
            }
            editTextSeed.setSelection(cursorPosition);
        }

        if (value >= 20 && value <= 70) {
            seekBarNumberTileToRemove.setProgress(value - 20);

            editTextNumberTileToRemove.setTextColor(Color.BLACK);
            editTextSeed.setTextColor(Color.BLACK);
            editTextSeed.setError(null);

            validButton.setEnabled(true);
        }
        else {
            editTextNumberTileToRemove.setTextColor(Color.RED);
            editTextSeed.setTextColor(Color.RED);
            editTextSeed.setError(getString(R.string.error_label_configure_sudoku_size_error));
            validButton.setEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
