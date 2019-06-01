package fr.pyjacpp.sudoku;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private final static long DELAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.logo);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int min = size.x > size.y ? size.y : size.x;

        logo.getLayoutParams().width = min / 2;
        logo.getLayoutParams().height = min / 2;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),
                        MainMenuActivity.class));
                finish();
            }
        }, DELAY_TIME);
    }
}
