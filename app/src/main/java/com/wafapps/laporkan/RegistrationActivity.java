package com.wafapps.laporkan;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {
    private EditText username, email,password,cnfpassword;
    private Button  btn_reg, btn_regLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Registrasi LaporKan");



        username = findViewById(R.id.regis_input_email);
        password = findViewById(R.id.regis_input_password);
        cnfpassword = findViewById(R.id.regis_input_password2);
        mAuth = FirebaseAuth.getInstance();
        btn_reg = findViewById(R.id.regis_btn_regis);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        btn_regLogin = findViewById(R.id.regis_btn_login);
        btn_regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();
                String pwd = password.getText().toString();
                String cnfPwd = cnfpassword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(!pwd.equals(cnfPwd)){
                    Toast.makeText(RegistrationActivity.this, "Password Harap Sama !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(cnfPwd)){
                    Toast.makeText(RegistrationActivity.this, "Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "Pengguna Terdaftar", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(RegistrationActivity.this, "Gagal Daftar!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }


            }
        });

    }
}
