package fr.pyjacpp.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Start animating the image
        final ImageView splash = findViewById(R.id.SudokuLogo);
        splash.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.logo_shake_animation));

        findViewById(R.id.PlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConfigureSudokuGridActivity.class));
            }
        });
    }
}
