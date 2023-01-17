package com.wafapps.laporkan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class splash extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread thread = new Thread(){
            public  void run(){
                try {
                    sleep(3000);

                } catch (InterruptedException e){
                    e.printStackTrace();

                } finally{
                    startActivity(new Intent(splash.this, LoginActivity.class));
                    finish();
                }
            }

        };
        thread.start();
    }
}
