package com.wafapps.laporkan.report;

public class KebakaranClass {
    private String kategori;
    private String namaPelapor;
    private String emailPelapor;
    private String noTelp;
    private String tglKejadian;
    private String lokasi;
    private String image;
    private String status;
    private String isi;
    private String kerusakan;
    private String sedia;
    private String tambahan;
    private String Key;

    public KebakaranClass() {
    }

    public KebakaranClass(String kategori, String namaPelapor, String emailPelapor, String noTelp, String tglKejadian, String lokasi, String image, String status, String isi, String kerusakan, String sedia, String tambahan) {
        this.kategori = kategori;
        this.namaPelapor = namaPelapor;
        this.emailPelapor = emailPelapor;
        this.noTelp = noTelp;
        this.tglKejadian = tglKejadian;
        this.lokasi = lokasi;
        this.image = image;
        this.status = status;
        this.isi = isi;
        this.kerusakan = kerusakan;
        this.sedia = sedia;
        this.tambahan = tambahan;
    }

    public String getEmailPelapor() {
        return emailPelapor;
    }

    public void setEmailPelapor(String emailPelapor) {
        this.emailPelapor = emailPelapor;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamaPelapor() {
        return namaPelapor;
    }

    public void setNamaPelapor(String namaPelapor) {
        this.namaPelapor = namaPelapor;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getTglKejadian() {
        return tglKejadian;
    }

    public void setTglKejadian(String tglKejadian) {
        this.tglKejadian = tglKejadian;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getKerusakan() {
        return kerusakan;
    }

    public void setKerusakan(String kerusakan) {
        this.kerusakan = kerusakan;
    }

    public String getSedia() {
        return sedia;
    }

    public void setSedia(String sedia) {
        this.sedia = sedia;
    }

    public String getTambahan() {
        return tambahan;
    }

    public void setTambahan(String tambahan) {
        this.tambahan = tambahan;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}

