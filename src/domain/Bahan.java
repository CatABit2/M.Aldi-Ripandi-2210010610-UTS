// =======================
// File: src/domain/Bahan.java
// =======================
package domain;

public class Bahan {
    private String teks;

    public Bahan(String teks) { this.teks = teks == null ? "" : teks; }
    public String getTeks() { return teks; }
    public void setTeks(String teks) { this.teks = teks == null ? "" : teks; }

    @Override public String toString() { return teks; }
}
