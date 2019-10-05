package fr.pyjacpp.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenuActivity extends AppCompatActivity {

    private Button continueButton;
    private Handler updateHandler;
    private Runnable updateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Start animating the image
        final ImageView logo = findViewById(R.id.SudokuLogo);

        logo.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.logo_shake_animation));

        continueButton = findViewById(R.id.continuePlayButton);
        final Button playButton = findViewById(R.id.playButton);
        final Button statisticsButton = findViewById(R.id.statisticsButton);
        final Button tutorialButton = findViewById(R.id.tutorialButton);
        final Button creditsButton = findViewById(R.id.creditsButton);
        final Button quitButton = findViewById(R.id.quitButton);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        SudokuActivity.class));
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        ConfigureSudokuGridActivity.class));

            }
        });
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        StatisticsActivity.class));
            }
        });
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        TutorialRulesActivity.class));
            }
        });
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        CreditsActivity.class));
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askQuit();
            }
        });

    }

    @Override
    protected void onStart() {
        if (!getSudokuApplication().preferencesIsLoad()) {
            updateHandler = new Handler();
            updateRunnable = new Runnable() {
                @Override
                public void run() {
                    if (getSudokuApplication().preferencesIsLoad()) {
                        updateHandler = null;
                        updateRunnable = null;
                        continueButton.setEnabled(
                                getSudokuApplication().getCurrentSudokuGrid() != null
                        );
                    } else {
                        updateHandler.postDelayed(updateRunnable, 100);
                    }
                }
            };

            updateHandler.postDelayed(updateRunnable, 100);
        }

        continueButton.setEnabled(getSudokuApplication().getCurrentSudokuGrid() != null);

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        askQuit();
    }

    private void askQuit() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_quit_title)
                .setMessage(R.string.dialog_quit_message)
                .setNegativeButton(R.string.dialog_quit_no, null)
                .setPositiveButton(R.string.dialog_quit_yes, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .show();
    }

    private SudokuApplication getSudokuApplication() {
        return (SudokuApplication) getApplicationContext();
    }
}
