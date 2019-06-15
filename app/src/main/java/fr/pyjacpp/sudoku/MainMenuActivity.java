package fr.pyjacpp.sudoku;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Start animating the image
        final ImageView logo = findViewById(R.id.SudokuLogo);

        logo.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.logo_shake_animation));

        final Button playButton = findViewById(R.id.playButton);
        final Button tutorialButton = findViewById(R.id.tutorialButton);
        final Button creditsButton = findViewById(R.id.creditsButton);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);


        logo.getLayoutParams().height = (int) (size.x * 0.625f);
        logo.getLayoutParams().width = (int) (size.x * 0.625f);

        playButton.setTextSize(size.x * 0.058333333f);
        tutorialButton.setTextSize(size.x * 0.041666667f);
        creditsButton.setTextSize(size.x * 0.0375f);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        ConfigureSudokuGridActivity.class));
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
    }
}
