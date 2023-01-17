package com.wafapps.laporkan;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ActivityUpdate extends AppCompatActivity {
    private EditText namaBaru, notelpBaru, lokasiBaru, isiBaru;
    private DatePickerDialog mDateSetListener;
    private SimpleDateFormat dateFormatter;
    private ImageView img;
    private String getUid,cbox,getEmail, updateKorban, status, kategori, cekNama, cekNotelp, cekTgl, cekLokasi, cekIsi, cekKerusakan, cekTambahan, cekKorban;
    private TextView result_dateBaru;
    private Button update, tglBaru;
    private RadioGroup rbKorban;
    private Spinner kerusakan;
    private RadioButton rbYa, rbTidak, radioButton;
    private CheckBox ck1, ck2;
    private ProgressBar progressBar;
    private RadioButton sediaYa, sediaTidak;

    ActivityResultLauncher<Intent> activityResultLauncher;
    private DatabaseReference database;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CHINA);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUid = user.getUid();
        getEmail = user.getEmail();


        getSupportActionBar().setTitle("Edit Laporan");
        namaBaru = findViewById(R.id.new_input_nama);
        notelpBaru = findViewById(R.id.new_input_notelp);
        tglBaru = findViewById(R.id.new_button_tgl);
        lokasiBaru = findViewById(R.id.new_input_lokasi);
        isiBaru = findViewById(R.id.new_input_laporan);
        img = findViewById(R.id.new_input_image);
        update = findViewById(R.id.new_btn_lapor);
        kerusakan = findViewById(R.id.spinner_kerusakan);
        result_dateBaru = findViewById(R.id.new_result_date);
        rbKorban = findViewById(R.id.rbKorban);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        rbYa = findViewById(R.id.new_rbYa);
        rbTidak = findViewById(R.id.new_rbTidak);
        ck1 = findViewById(R.id.new_ckMedis);
        ck2 = findViewById(R.id.new_ckKend);

        ck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "Tim Medis";
            }
        });

        ck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "Kendaraan Lain";
            }
        });


        kategori = "KEBAKARAN";
        status = "BELUM DITERIMA";



        database = FirebaseDatabase.getInstance().getReference();
        getData();
//        getData1();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cekNama = namaBaru.getText().toString().toUpperCase();
                cekNotelp = notelpBaru.getText().toString();
                int cekselected = rbKorban.getCheckedRadioButtonId();
                radioButton = findViewById(cekselected);
                cekKorban = (String) radioButton.getText();
                cekTambahan = null;
                if(ck1.isChecked()){
                    cekTambahan = ck1.getText().toString();
                }
                if(ck2.isChecked()){
                    cekTambahan = ck2.getText().toString();
                }
                if(ck1.isChecked() && ck2.isChecked()){
                    cekTambahan = "Tim Medis, Kendaraan Lain";
                }
//                                        cekTgl = tglBaru.getText().toString();
                cekKerusakan = kerusakan.getSelectedItem().toString();
                cekLokasi = lokasiBaru.getText().toString();

                if(isEmpty(cekNama) || isEmpty(cekNotelp) || isEmpty(cekLokasi)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivityUpdate.this, "Harap isi dengan benar", Toast.LENGTH_SHORT).show();
                }else {
                    KebakaranClass setLaporan = new KebakaranClass();
                    setLaporan.setKategori(kategori);
                    setLaporan.setStatus(status);
                    setLaporan.setNamaPelapor(namaBaru.getText().toString().toUpperCase());
                    setLaporan.setEmailPelapor(getEmail);
                    setLaporan.setNoTelp(notelpBaru.getText().toString());
                    setLaporan.setTglKejadian(result_dateBaru.getText().toString());
                    setLaporan.setLokasi(lokasiBaru.getText().toString());
                    setLaporan.setKerusakan(kerusakan.getSelectedItem().toString());
                    setLaporan.setIsi(isiBaru.getText().toString());
                    setLaporan.setTambahan(cekTambahan);
                    setLaporan.setSedia(cekKorban);
                    final String getImage = getIntent().getExtras().getString("dataImage");
                    setLaporan.setImage(getImage);
                    updateLaporan(setLaporan);
                    progressBar.setVisibility(View.GONE);


                }

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK && result.getData() !=null){
                imageUri = result.getData().getData();

//                final String getImage = getIntent().getExtras().getString("dataImage");
//                StorageReference filePath = storage.getReferenceFromUrl(getImage);
//                filePath.putFile(imageUri);



                Picasso.get().load(imageUri).into(img);

                Log.e("Data Gambar ", String.valueOf(imageUri) );
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(ActivityUpdate.this, "Data Sedang Diunggah", Toast.LENGTH_SHORT).show();
                        StorageReference filePath = storage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
                        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        cekNama = namaBaru.getText().toString().toUpperCase();
                                        cekNotelp = notelpBaru.getText().toString();
                                        cekTambahan = null;
                                        if(ck1.isChecked()){
                                            cekTambahan = ck1.getText().toString();
                                        }
                                        if(ck2.isChecked()){
                                            cekTambahan = ck2.getText().toString();
                                        }
                                        if(ck1.isChecked() && ck2.isChecked()){
                                            cekTambahan = "Tim Medis, Kendaraan Lain";
                                        }
//                                        cekTgl = tglBaru.getText().toString();
                                        cekKerusakan = kerusakan.getSelectedItem().toString();
                                        cekLokasi = lokasiBaru.getText().toString();

                                        if(isEmpty(cekNama) || isEmpty(cekNotelp) || isEmpty(cekLokasi)){
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(ActivityUpdate.this, "Harap isi dengan benar", Toast.LENGTH_SHORT).show();
                                        }else {
                                            KebakaranClass setLaporan = new KebakaranClass();
                                            setLaporan.setKategori(kategori);
                                            setLaporan.setStatus(status);
                                            setLaporan.setNamaPelapor(namaBaru.getText().toString().toUpperCase());
                                            setLaporan.setNoTelp(notelpBaru.getText().toString());
                                            setLaporan.setTglKejadian(result_dateBaru.getText().toString());
                                            setLaporan.setLokasi(lokasiBaru.getText().toString());
                                            setLaporan.setKerusakan(kerusakan.getSelectedItem().toString());
                                            setLaporan.setIsi(isiBaru.getText().toString());
                                            int UpdateSelectedKorban = rbKorban.getCheckedRadioButtonId();
                                            radioButton = findViewById(UpdateSelectedKorban);
                                            updateKorban = (String) radioButton.getText();
                                            setLaporan.setTambahan(cekTambahan);
                                            setLaporan.setSedia(updateKorban);
                                            setLaporan.setImage(task.getResult().toString());
                                            updateLaporan(setLaporan);
                                            progressBar.setVisibility(View.GONE);


                                        }

                                    }
                                });
                            }
                        });
                    }
                });

            }
        });

        tglBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });




    }

    private void showDateDialog() {
        Calendar newCalender = Calendar.getInstance();

        mDateSetListener = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                result_dateBaru.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

        mDateSetListener.show();
    }

    private void updateLaporan(KebakaranClass setLaporan) {
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Admin")
                .child(getUid)
                .child(getKey)
                .setValue(setLaporan)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        namaBaru.setText("");
                        notelpBaru.setText("");
                        lokasiBaru.setText("");
                        isiBaru.setText("");

                        Toast.makeText(ActivityUpdate.this, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }

//    private void getData1(){
//        final String getImage = getIntent().getExtras().getString("dataImage");
//        Picasso.get().load(getImage).into(img);
//        img.setImageURI(imageUri);
////        final String getImage = getIntent().getExtras().getString("dataImage");
////        StorageReference filePath = storage.getReferenceFromUrl(getImage);
////        filePath.putFile(imageUri);
//    }

    private void getData() {
        final String getNama = getIntent().getExtras().getString("dataPelapor");
        final String getNotelp = getIntent().getExtras().getString("dataNotelp");
        final String getTgl = getIntent().getExtras().getString("dataTgl");
        final String getLokasi = getIntent().getExtras().getString("dataLokasi");
        final String getIsi = getIntent().getExtras().getString("dataIsi");
        final String getKerusakan = getIntent().getExtras().getString("dataKerusakan");
        final String getKorban = getIntent().getExtras().getString("dataKorban");
        final String getTambahan = getIntent().getExtras().getString("dataTambahan");
        final String getImage = getIntent().getExtras().getString("dataImage");

        namaBaru.setText(getNama);
        notelpBaru.setText(getNotelp);
        result_dateBaru.setText(getTgl);
        lokasiBaru.setText(getLokasi);
        kerusakan.setSelection(((ArrayAdapter<String>)kerusakan.getAdapter()).getPosition(getKerusakan));
        isiBaru.setText(getIsi);

        if (getKorban != null && getKorban.equalsIgnoreCase("Ya,Ada")){
            rbKorban.check(R.id.new_rbYa);
        }else if(getKorban != null && getKorban.equalsIgnoreCase("Tidak Tahu")){
            rbKorban.check(R.id.new_rbTidak);
        }

        if(getTambahan != null && getTambahan.equalsIgnoreCase("Tim Medis")){
            ck1.setChecked(true);
        }else if(getTambahan != null && getTambahan.equalsIgnoreCase("Kendaraan Lain")){
            ck2.setChecked(true);
        }else if(getTambahan != null && getTambahan.equalsIgnoreCase("Tim Medis, Kendaraan Lain")){
            ck1.setChecked(true);
            ck2.setChecked(true);

        }
        Picasso.get().load(getImage).into(img);



    }
}