package com.wafapps.laporkan;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AccountInfomation extends AppCompatActivity {

    private TextView showEmail, showID;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private InterstitialAd mInterstitialAd;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_infomation);
        getSupportActionBar().setTitle("Informasi Akun");
        img = findViewById(R.id.imageView);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-6717854542875869/1640137070", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AccountInfomation.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });



        showEmail = findViewById(R.id.showEmail);
        showID = findViewById(R.id.showID);
        btnLogout = findViewById(R.id.button_logout2);

        mAuth = FirebaseAuth.getInstance();



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    for(UserInfo profile : user.getProviderData()){
                        String email = profile.getEmail();
                        showEmail.setText(email);
                        Toast.makeText(AccountInfomation.this, "User "+email+" Telah Keluar", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();

                    }
                }
                Intent intent = new Intent(AccountInfomation.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            for(UserInfo profile : user.getProviderData()){
                String email = profile.getEmail();
                String id = profile.getPhoneNumber();
                showEmail.setText(email);
                showID.setText(id);
//                Toast.makeText(this, "User "+email+" Logged Out.. ", Toast.LENGTH_SHORT).show();
//                mAuth.signOut();
            }
        }
    }
}