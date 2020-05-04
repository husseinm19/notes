package husseinm19.github.com.taskmanagement;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Hussein on 24-04-2020
 */
public class SplashScreen extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        ProgressBar progressBar= (ProgressBar) findViewById(R.id.intro_progress);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        //getSupportActionBar().hide();
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                SplashScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                        finish();

                    }
                });
            }
        }, 3000);



//        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
//        Sprite doubleBounce = new DoubleBounce();
//        Sprite cubeGrid = new CubeGrid();
//        progressBar.setIndeterminateDrawable(cubeGrid);
//
//        Timer myTimer = new Timer();
//        myTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                // If you want to modify a view in your Activity
//                SplashScreen.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(SplashScreen.this,LoginActivity.class));
//                        finish();
//
//
//
////end
//                    }
//                });
//            }
//        }, 3000);

    }

}


