package com.wafapps.laporkan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.squareup.picasso.Picasso;
import com.wafapps.laporkan.report.KebakaranClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final ArrayList<KebakaranClass> listLaporan;
    private final Context context;

    public interface dataListener{
        void onDeleteData(KebakaranClass data, int position);
    }

    dataListener listener;

    public RecyclerViewAdapter(ArrayList<KebakaranClass> listLaporan, Context context){
        this.listLaporan = listLaporan;
        this.context = context;
        listener =(ActivityListData)context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
//        ViewHolder viewHolder = new ViewHolder(v, listener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String Kategori = listLaporan.get(position).getKategori();
        final String Status = listLaporan.get(position).getStatus();
        final String Nama = listLaporan.get(position).getNamaPelapor();
        final String Email = listLaporan.get(position).getEmailPelapor();
        final String Notelp = listLaporan.get(position).getNoTelp();
        final String Tgl = listLaporan.get(position).getTglKejadian();
        final String Lokasi = listLaporan.get(position).getLokasi();
        final String Isi = listLaporan.get(position).getIsi();
        final String Image = listLaporan.get(position).getImage();
        final String Kerusakan = listLaporan.get(position).getKerusakan();
        final String Korban = listLaporan.get(position).getSedia();
        final String Tambahan = listLaporan.get(position).getTambahan();
//        final String Waktu = listLaporan.get(position).getWaktu();



        holder.kategori.setText("KATEGORI : " + Kategori);
        holder.status.setText("STATUS : " + Status);
        holder.nama.setText("Nama Pelapor : "+ Nama);
        holder.email.setText("Email Pelapor  : "+ Email);
        holder.notelp.setText("Nomor Telepon : "+ Notelp);
        holder.tgl.setText("Tanggal Kejadian : "+ Tgl);
        holder.lokasi.setText("Lokasi :" + Lokasi);
        holder.isi.setText("Isi Laporan : "+ Isi);
        holder.kerusakan.setText("Kerusakan : "+Kerusakan);
        holder.korban.setText("Kemungkinan Korban : " + Korban);
        holder.tambahan.setText("Bantuan Tambahan : "+ Tambahan);
//        holder.time.setText("Waktu Lapor : " + Waktu);
        Picasso.get().load(Image).into(holder.img);

        holder.listItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Edit", "Hapus"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            Bundle bundle = new Bundle();
                            bundle.putString("dataKategori", listLaporan.get(position).getKategori());
                            bundle.putString("dataStatus", listLaporan.get(position).getStatus());
                            bundle.putString("dataPelapor", listLaporan.get(position).getNamaPelapor());
                            bundle.putString("dataEmail", listLaporan.get(position).getEmailPelapor());
                            bundle.putString("dataNotelp", listLaporan.get(position).getNoTelp());
                            bundle.putString("dataTgl", listLaporan.get(position).getTglKejadian());
                            bundle.putString("dataLokasi", listLaporan.get(position).getLokasi());
                            bundle.putString("dataIsi", listLaporan.get(position).getIsi());
                            bundle.putString("dataKerusakan", listLaporan.get(position).getKerusakan());
                            bundle.putString("dataSedia", listLaporan.get(position).getSedia());
                            bundle.putString("getPrimaryKey", listLaporan.get(position).getKey());
                            bundle.putString("dataKorban", listLaporan.get(position).getSedia());
                            bundle.putString("dataTambahan", listLaporan.get(position).getTambahan());
                            bundle.putString("dataImage", listLaporan.get(position).getImage());
                            Intent intent = new Intent(view.getContext(), ActivityUpdate.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                        if (i == 1){
                            listener.onDeleteData(listLaporan.get(position), position);
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });




    }

    @Override
    public int getItemCount() {
        return listLaporan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView kategori, status, nama,email, notelp, tgl, lokasi, isi, time, kerusakan, tambahan, korban ;
        private ImageView img;
        private LinearLayout listItem;
        dataListener listener;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
//            this.listener = listener;

            kategori = itemView.findViewById(R.id.view_kategori);
            status = itemView.findViewById(R.id.view_status);
            nama = itemView.findViewById(R.id.view_pelapor);
            email = itemView.findViewById(R.id.view_email);
            notelp = itemView.findViewById(R.id.view_notelp);
            tgl = itemView.findViewById(R.id.view_tgl);
            lokasi = itemView.findViewById(R.id.view_lokasi);
            isi = itemView.findViewById(R.id.view_isi);
            img = itemView.findViewById(R.id.view_imageView);
            kerusakan = itemView.findViewById(R.id.view_kerusakan);
            korban = itemView.findViewById(R.id.view_korban);
            tambahan = itemView.findViewById(R.id.view_tambahan);

//            time = itemView.findViewById(R.id.view_waktu);

            listItem = itemView.findViewById(R.id.list_item);
        }
    }
}
