// =======================
// File: src/domain/Resep.java
// =======================
package domain;

import java.util.ArrayList;
import java.util.List;

public class Resep {
    private String judul;
    private String kategori;
    private int porsi;
    private int durasiMenit;
    private List<Bahan> bahan;
    private List<Langkah> langkah;

    public Resep(String judul, String kategori, int porsi, int durasiMenit,
                 List<Bahan> bahan, List<Langkah> langkah) {
        this.judul = judul;
        this.kategori = kategori;
        this.porsi = porsi;
        this.durasiMenit = durasiMenit;
        this.bahan = bahan == null ? new ArrayList<>() : new ArrayList<>(bahan);
        this.langkah = langkah == null ? new ArrayList<>() : new ArrayList<>(langkah);
    }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public int getPorsi() { return porsi; }
    public void setPorsi(int porsi) { this.porsi = porsi; }

    public int getDurasiMenit() { return durasiMenit; }
    public void setDurasiMenit(int durasiMenit) { this.durasiMenit = durasiMenit; }

    public List<Bahan> getBahan() { return bahan; }
    public void setBahan(List<Bahan> bahan) {
        this.bahan = bahan == null ? new ArrayList<>() : new ArrayList<>(bahan);
    }

    public List<Langkah> getLangkah() { return langkah; }
    public void setLangkah(List<Langkah> langkah) {
        this.langkah = langkah == null ? new ArrayList<>() : new ArrayList<>(langkah);
    }
}
