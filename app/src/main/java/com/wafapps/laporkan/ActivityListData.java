package com.wafapps.laporkan;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wafapps.laporkan.report.KebakaranClass;

import java.util.ArrayList;

public class ActivityListData extends AppCompatActivity implements RecyclerViewAdapter.dataListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String getUid;

    private DatabaseReference reference;
    private ArrayList<KebakaranClass> dataLaporan;

    SearchView searchView;

    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        getSupportActionBar().setTitle("Daftar Laporan");

        recyclerView = findViewById(R.id.datalist);
        progressBar = findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.GONE);
        searchView = findViewById(R.id.etSearch);
        searchView.setQueryHint("Cari Data...");
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUid = user.getUid();

        GetData();
        MyRecyclerView();

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(TextUtils.isEmpty(s)){
                        GetData();
                    }else{
                        GetData1(s);
                    }
                    return true;
                }
            });
        }
    }

    private void GetData1(String data) {
        progressBar.setVisibility(View.VISIBLE);
//        Toast.makeText(getApplicationContext(), "Mohon Tunggu Sebentar", Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child(getUid).orderByChild("namaPelapor").startAt(data.toUpperCase()).endAt(data.toUpperCase() + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataLaporan = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren() ){
                            KebakaranClass laporanKebakaran = snapshot.getValue(KebakaranClass.class);

                            laporanKebakaran.setKey(snapshot.getKey());
                            dataLaporan.add(laporanKebakaran);
                        }

                        adapter = new RecyclerViewAdapter(dataLaporan, ActivityListData.this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ActivityListData.this, "Gagal di muat", Toast.LENGTH_SHORT).show();
                        Log.e("MyListActivity", error.getDetails()+" "+error.getMessage());

                    }
                });
    }


    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }



    private void GetData() {
        progressBar.setVisibility(View.VISIBLE);
//        Toast.makeText(getApplicationContext(), "Mohon Tunggu Sebentar", Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child(getUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataLaporan = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren() ){
                            KebakaranClass laporanKebakaran = snapshot.getValue(KebakaranClass.class);

                            laporanKebakaran.setKey(snapshot.getKey());
                            dataLaporan.add(laporanKebakaran);
                        }

                        adapter = new RecyclerViewAdapter(dataLaporan, ActivityListData.this);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ActivityListData.this, "Gagal di muat", Toast.LENGTH_SHORT).show();
                        Log.e("MyListActivity", error.getDetails()+" "+error.getMessage());

                    }
                });
    }

    public void onDeleteData(KebakaranClass data, int position){
        if(reference != null){
            reference.child("Admin")
                    .child(getUid)
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ActivityListData.this, "Laporan Berhasil Dihapus !", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}