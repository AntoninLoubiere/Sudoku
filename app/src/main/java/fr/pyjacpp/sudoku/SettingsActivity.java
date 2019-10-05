package fr.pyjacpp.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ((RadioGroup) findViewById(R.id.sortNotesGroup)).check(
                getApplicationSudoku().getCurrentSudokuGrid().getSortNotes()
                        ? R.id.sortNotesSort : R.id.sortNotesAdd
        );



        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        findViewById(R.id.validButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();
            }
        });
    }

    private SudokuApplication getApplicationSudoku() {
        return (SudokuApplication) getApplicationContext();
    }

    @Override
    public boolean onSupportNavigateUp() {
        cancel();
        return true;
    }

    private void cancel() {
        finish();
    }

    private void valid() {
        getApplicationSudoku().getCurrentSudokuGrid().setSortNotes(
                ((RadioButton) findViewById(R.id.sortNotesSort)).isChecked()
        );
        finish();
    }
}
