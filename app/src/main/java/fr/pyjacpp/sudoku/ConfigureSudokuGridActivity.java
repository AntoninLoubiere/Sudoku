package fr.pyjacpp.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

public class ConfigureSudokuGridActivity extends AppCompatActivity {

    SeekBar seekBarNumberTileToRemove;
    EditText editTextNumberTileToRemove;
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

        seekBarNumberTileToRemove = findViewById(R.id.seekBarNumberTileToRemove);
        editTextNumberTileToRemove = findViewById(R.id.editTextNumberTileToRemove);
        validButton = findViewById(R.id.validButton);
        showConflictSwitch = findViewById(R.id.switchShowConflict);

        seekBarNumberTileToRemove.setProgress(numberTileToRemove - 20);
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

                    if (input >= 2 && input <= 70) {
                        seekBarNumberTileToRemove.setProgress(input - 20);

                        editTextNumberTileToRemove.setTextColor(Color.BLACK);
                        validButton.setEnabled(true);
                        return;
                    }

                } catch (NumberFormatException ignored) { }

                editTextNumberTileToRemove.setTextColor(Color.RED);
                validButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

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

                startActivity(sudokuIntent);
                finish();
            }
        });
    }
}
