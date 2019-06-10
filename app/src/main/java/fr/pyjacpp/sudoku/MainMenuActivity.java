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
        final Button creditsButton = findViewById(R.id.creditsButton);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        int width = size.x;
        int height = size.y;
        int min = width > height ? height : width; // get min


        logo.getLayoutParams().height = (int) (min * 0.625f);
        logo.getLayoutParams().width = (int) (min * 0.625f);

        playButton.setTextSize(min * 0.058333333f);
        creditsButton.setTextSize(min * 0.0375f);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        ConfigureSudokuGridActivity.class));
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
