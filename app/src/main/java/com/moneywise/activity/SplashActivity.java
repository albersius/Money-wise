package com.moneywise.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.moneywise.R;
import com.moneywise.constant.Constant;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(() -> {
            int id = getCurrentUserId();

            Log.i("TOLOL", String.valueOf(id));

            Intent intent;

            if(id != -1) {
               intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 3000);
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPref.getInt(Constant.ID_USER_KEY, -1);
    }
}