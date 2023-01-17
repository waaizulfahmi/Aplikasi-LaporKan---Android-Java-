package com.wafapps.laporkan;

import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wafapps.laporkan.report.KebakaranClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {
    private DatePickerDialog mDateSetListener;
    private SimpleDateFormat dateFormatter;
    private TextView uploadGambar, email;
    private EditText nama, notelp, tgl, lokasi, isi_laporan;
    private String getUid, getKategori,getNama,getEmail, getNotelp, getTgl, getLokasi, getIsi, getImage, getStatus, getKerusakan, getSedia, getTambahan;
    private Button btn_tgl, submitLaporan;
    private CheckBox ck1, ck2;
    private ImageView imageKej;
    private RadioButton sediaYa, sediaTidak;
    private Spinner spinner;
    private TextView result_date, time;
    private ProgressBar progressBar;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri imageUri;
    private FirebaseStorage storage2 = FirebaseStorage.getInstance();

    DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");


        getSupportActionBar().setTitle(title);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CHINA);




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUid = user.getUid();
        getEmail = user.getEmail();

        

        nama = findViewById(R.id.input_nama);
        notelp = findViewById(R.id.input_notelp);
        lokasi = findViewById(R.id.input_lokasi);
        btn_tgl = findViewById(R.id.button_tgl);
        isi_laporan = findViewById(R.id.input_laporan);
        imageKej = findViewById(R.id.input_image);
        submitLaporan = findViewById(R.id.btn_lapor);
        sediaYa = findViewById(R.id.rbYa);
        sediaTidak = findViewById(R.id.rbTidak);
        spinner = findViewById(R.id.spinner_kerusakan);
        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.GONE);
        uploadGambar = findViewById(R.id.uploadgmbr);
        ck1 = findViewById(R.id.ckMedis);
        ck2 = findViewById(R.id.ckKend);
//        time = findViewById(R.id.view_waktu);


        getReference = database.getReference();

        imageKej.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
//                UploadImage();

            }
        });

        getSedia = null;
        if(sediaYa.isChecked()){
            getSedia = sediaYa.getText().toString();

        }
        if(sediaTidak.isChecked()){
            getSedia = sediaTidak.getText().toString();
            submitLaporan.setEnabled(false);
        }




        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if (result.getResultCode() == RESULT_OK && result.getData() != null){
                imageUri = result.getData().getData();
                Log.e("Test", String.valueOf(imageUri));
                Picasso.get().load(imageUri).into(imageKej);

            }

            submitLaporan.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(ReportActivity.this, "Data Sedang diunggah", Toast.LENGTH_SHORT).show();
                    StorageReference filePath = storage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String tassk = task.getResult().toString();

//                                    UploadImage();
                                    Intent intent = getIntent();
                                    String setkategori = intent.getStringExtra("kategori");
                                    getKategori = setkategori;
//                                    if(getSupportActionBar().getTitle() == "Laporkan Kebakaran"){
//                                        getKategori = "KEBAKARAN";
//
//                                    }else if(getSupportActionBar().getTitle() == "Laporkan Kriminal"){
//                                        getKategori = "KRIMINAL";
//                                    }else if(getSupportActionBar().getTitle() == "Laporkan Bencana"){
//                                        getKategori = "BENCANA";
//                                    }

                                    getNama = nama.getText().toString().toUpperCase();
                                    getEmail = getEmail.toString();
                                    getNotelp = notelp.getText().toString();
                                    getTgl = result_date.getText().toString();
                                    getLokasi = lokasi.getText().toString();
                                    getIsi = isi_laporan.getText().toString();
                                    getStatus = "BELUM DITERIMA";
                                    getKerusakan = spinner.getSelectedItem().toString();
                                    getSedia = null;
                                    if(sediaYa.isChecked()){
                                        getSedia = sediaYa.getText().toString();

                                    }
                                    if(sediaTidak.isChecked()){
                                        getSedia = sediaTidak.getText().toString();
                                        submitLaporan.setEnabled(false);
                                    }

                                    getTambahan = null;
                                    if(ck1.isChecked()){
                                        getTambahan = ck1.getText().toString();
                                    }
                                    if(ck2.isChecked()){
                                        getTambahan = ck2.getText().toString();
                                    }
                                    if(ck1.isChecked() && ck2.isChecked()){
                                        getTambahan = "Tim Medis, Kendaraan Lain";
                                    }
                                    getImage = task.getResult().toString();


                                    if(isEmpty(getNama) || isEmpty(getNotelp) || isEmpty(getLokasi) || isEmpty(getIsi)){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ReportActivity.this, "Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                                    }else{
                                        checkData();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }
                    });

                }
            });
        });

        result_date = findViewById(R.id.result_date);
        btn_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

    }



    @SuppressLint("NewApi")
    private void checkData() {
        getReference.child("Admin").child(getUid).push()
                .setValue(new KebakaranClass(getKategori,getNama,getEmail, getNotelp,getTgl, getLokasi,getImage, getStatus,getIsi, getKerusakan, getSedia, getTambahan))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        nama.setText("");
                        notelp.setText("");
                        spinner.setSelected(true);
                        tgl.setText("");
                        lokasi.setText("");
                        isi_laporan.setText("");


                        Toast.makeText(ReportActivity.this, "Data Kamu Telah Terkirim Tunggu Untuk Konfirmasi Laporan", Toast.LENGTH_SHORT).show();
                    }
                });
    }


//    Menampilkan Time

//    private void timeSet(){
//        Calendar calendar = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(" d MMM yyyy HH:mm:ss");
//        String setTime = format.format(calendar.getTime());
//        time.setText(setTime);
//    }


    //    MENAMPILKAN TANGGAL
    private void showDateDialog() {
        Calendar newCalender = Calendar.getInstance();

        mDateSetListener = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                result_date.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

        mDateSetListener.show();

    }


}