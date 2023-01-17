package com.wafapps.laporkan;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity  {

    private ImageView btn_kebakaran,btn_kriminal, btn_bencana, btn_list, btn_logout, btn_info;
    private FirebaseAuth mAuth;
    private AlertDialog.Builder builder;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        getSupportActionBar().setTitle("Menu Utama");


        btn_kebakaran = findViewById(R.id.button_kebakaran);
        btn_kriminal = findViewById(R.id.button_kriminal);
        btn_bencana = findViewById(R.id.button_bencana);
        btn_info = findViewById(R.id.button_info);
        btn_list = findViewById(R.id.button_list);
        btn_logout = findViewById(R.id.button_logout);
        builder = new AlertDialog.Builder(this);

        mAuth = FirebaseAuth.getInstance();
        btn_kebakaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                intent.putExtra("title", "Laporkan Kebakaran");
                intent.putExtra("kategori", "KEBAKARAN");
//                getSupportActionBar().setTitle("Laporkan Kebakaran");
                startActivity(intent);
            }
        });
        btn_kriminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
//                getSupportActionBar().setTitle("Laporkan Kriminal");
                intent.putExtra("title", "Laporkan Kriminal");
                intent.putExtra("kategori", "KRIMINAL");
                startActivity(intent);

            }
        });

        btn_bencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
//                getSupportActionBar().setTitle("Laporkan Bencana");
                intent.putExtra("title", "Laporkan Bencana");
                intent.putExtra("kategori", "BENCANA");
                startActivity(intent);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage(R.string.dialog_start_logout)
                        .setTitle("Pesan Logout")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null){
                                    for(UserInfo profile : user.getProviderData()){
                                        String email = profile.getEmail();
                                        Toast.makeText(getApplication(), "User "+email+" Logged Out.. ", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }
                                }

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();


            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, ActivityListData.class);
                startActivity(intent1);
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, AccountInfomation.class);
                startActivity(intent2);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return  true;



//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.about:
                Intent intent3 = new Intent(MainActivity.this, About.class);
                startActivity(intent3);
                return true;

            case R.id.logout:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    for(UserInfo profile : user.getProviderData()){
                        String email = profile.getEmail();
                        Toast.makeText(this, "User "+email+" Logged Out.. ", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.history:
                Intent intent1 = new Intent(MainActivity.this, ActivityListData.class);
                startActivity(intent1);
                return true;
            case R.id.account:
                Intent intent2 = new Intent(MainActivity.this, AccountInfomation.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}