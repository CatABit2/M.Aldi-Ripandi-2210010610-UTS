package util;

import domain.Bahan;
import domain.Langkah;
import domain.Resep;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class TxtResepIO {
    private TxtResepIO(){}

    // enum HARUS di level kelas, bukan di dalam method
    private enum Mode { NONE, BAHAN, LANGKAH }

    public static List<Resep> read(File f) throws IOException {
        List<Resep> out = new ArrayList<>();
        List<String> lines = java.nio.file.Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);

        String judul = null, kategori = null;
        int porsi = 0, durasi = 0;
        List<Bahan>  bahan   = new ArrayList<>();
        List<Langkah> langkah = new ArrayList<>();
        Mode mode = Mode.NONE;

        for (String raw : lines) {
            String s = raw == null ? "" : raw.trim();

            if (s.equalsIgnoreCase("=== RESEP ===")) {
                // push sebelumnya kalau lengkap
                pushIfReady(out, judul, kategori, porsi, durasi, bahan, langkah);
                // reset penampung
                judul = null; kategori = null; porsi = 0; durasi = 0;
                bahan.clear(); langkah.clear(); mode = Mode.NONE;
                continue;
            }

            if (s.startsWith("JUDUL:"))    { judul = s.substring(6).trim();    continue; }
            if (s.startsWith("KATEGORI:")) { kategori = s.substring(9).trim(); continue; }
            if (s.startsWith("PORSI:"))    { porsi = safeInt(s.substring(6).trim());  continue; }
            if (s.startsWith("DURASI:"))   { durasi = safeInt(s.substring(7).trim()); continue; }

            if (s.equalsIgnoreCase("#BAHAN"))   { mode = Mode.BAHAN;   continue; }
            if (s.equalsIgnoreCase("#LANGKAH")) { mode = Mode.LANGKAH; continue; }

            if (mode == Mode.BAHAN   && s.startsWith("-")) { bahan.add(new Bahan(cleanBullet(s)));   continue; }
            if (mode == Mode.LANGKAH && s.startsWith("*")) { langkah.add(new Langkah(cleanBullet(s))); continue; }
        }
        // dorong terakhir kalau ada
        pushIfReady(out, judul, kategori, porsi, durasi, bahan, langkah);
        return out;
    }

    private static void pushIfReady(List<Resep> out,
                                    String judul, String kategori, int porsi, int durasi,
                                    List<Bahan> bahan, List<Langkah> langkah) {
        if (judul != null && kategori != null) {
            out.add(new Resep(judul, kategori, porsi, durasi,
                    new ArrayList<>(bahan), new ArrayList<>(langkah)));
        }
    }

    public static void write(File f, List<Resep> list) throws IOException {
        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {

            for (Resep r : nzList(list)) {
                w.write("=== RESEP ==="); w.newLine();
                w.write("JUDUL: "   + nz(r.getJudul()));       w.newLine();
                w.write("KATEGORI: "+ nz(r.getKategori()));    w.newLine();
                w.write("PORSI: "   + r.getPorsi());           w.newLine();
                w.write("DURASI: "  + r.getDurasiMenit());     w.newLine();

                w.write("#BAHAN"); w.newLine();
                for (Bahan b : nzList(r.getBahan())) {
                    w.write("- " + nz(b == null ? null : b.getTeks())); w.newLine();
                }

                w.write("#LANGKAH"); w.newLine();
                for (Langkah l : nzList(r.getLangkah())) {
                    w.write("* " + nz(l == null ? null : l.getTeks())); w.newLine();
                }
                w.newLine();
            }
        }
    }

    private static int safeInt(String s){ try { return Integer.parseInt(s); } catch(Exception e){ return 0; } }
    private static String cleanBullet(String s){ return s.length()<=1 ? "" : s.substring(1).trim(); }
    private static String nz(String s){ return s==null ? "" : s; }
    private static <T> List<T> nzList(List<T> x){ return x==null ? Collections.<T>emptyList() : x; }
}
