package fr.pyjacpp.sudoku;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private final static long DELAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.logo);
        TextView version = findViewById(R.id.version);
        TextView author = findViewById(R.id.authorTextView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        logo.getLayoutParams().width = size.x / 2;
        logo.getLayoutParams().height = size.x / 2;

        author.setTextSize(size.x * 0.045833333f);
        version.setTextSize(size.x * 0.072916667f);

        progressBar.getLayoutParams().width = (int) (size.x * 0.208333333f);
        progressBar.getLayoutParams().height = (int) (size.x * 0.208333333f);

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
