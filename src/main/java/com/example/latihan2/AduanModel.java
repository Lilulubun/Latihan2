package com.example.latihan2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AduanModel {
    private String profil;
    private String judul;
    private String waktuTempat;
    private String tautanCepat;
    private String status;
    private String detil;
    private String kategori;
    private String kota;

    public AduanModel(String profil, String informasiUmum, String waktuTempat, String tautanCepat, String status, String detil) {
        this.profil = profil;
        this.judul = extractJudul(informasiUmum);
        this.kategori = extractKategori(informasiUmum);
        this.kota = extractKota(waktuTempat);
        this.waktuTempat = waktuTempat;
        this.tautanCepat = tautanCepat;
        this.status = status;
        this.detil = detil;
    }
    private String extractJudul(String informasiUmum) {
        // Assuming informasiUmum format is "judul - kategori"
        return informasiUmum.split(" - ")[0];
    }

    private String extractKategori(String informasiUmum) {
        // Assuming informasiUmum format is "judul - kategori"
        return informasiUmum.split(" - ")[1];
    }

    private String extractKota(String waktuTempat) {
        // Assuming kota is the first word in waktuTempat
        String[] parts = waktuTempat.split(" ");
        if (parts.length > 0) {
            return parts[0];
        }
        return "";
    }
    public AduanModel() {
    }

    public String getKategori() {
        return kategori;
    }

    public String getKota() {
        return kota;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getWaktuTempat() {
        return waktuTempat;
    }

    public void setWaktuTempat(String waktuTempat) {
        this.waktuTempat = waktuTempat;
    }

    public String getTautanCepat() {
        return tautanCepat;
    }

    public void setTautanCepat(String tautanCepat) {
        this.tautanCepat = tautanCepat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetil() {
        return detil;
    }

    public void setDetil(String detil) {
        this.detil = detil;
    }

    @Override
    public String toString() {
        return profil + "," + judul + "," + waktuTempat + "," + tautanCepat + "," + status + "," + detil;
    }
    public String toCSVString() {
        return String.join(",", profil, judul, waktuTempat, tautanCepat, status, detil);
    }
    public String mapToString() {
        return toString();
    }
    public static final CSVRowMapper<AduanModel> MAPPER = new CSVRowMapper<>() {
        @Override
        public AduanModel mapRow(String[] row) {
            return new AduanModel(row[0], row[1], row[2], row[3], row[4], row[5]);
        }

        @Override
        public String[] mapRowBack(AduanModel item) {
            return new String[]{
                    item.getProfil(),
                    item.getJudul(),
                    item.getWaktuTempat(),
                    item.getTautanCepat(),
                    item.getStatus(),
                    item.getDetil()
            };
        }
    };
}
