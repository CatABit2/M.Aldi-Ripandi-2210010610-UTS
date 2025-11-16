// =======================
// File: src/domain/Langkah.java
// =======================
package domain;

public class Langkah {
    private String teks;

    public Langkah(String teks) { this.teks = teks == null ? "" : teks; }
    public String getTeks() { return teks; }
    public void setTeks(String teks) { this.teks = teks == null ? "" : teks; }

    @Override public String toString() { return teks; }
}
