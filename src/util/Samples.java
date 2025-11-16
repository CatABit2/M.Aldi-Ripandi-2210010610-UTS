// =======================
// File: src/util/Samples.java
// =======================
package util;

import domain.Bahan;
import domain.Langkah;
import domain.Resep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Samples {
    private Samples() {}

    public static Resep rNasiGoreng() {
        return new Resep(
                "Nasi Goreng Rumahan", "Nasi", 2, 20,
                bahan("2 piring nasi","2 siung bawang putih","3 sdm kecap","1 butir telur","garam","minyak"),
                langkah("Tumis bawang putih","Masukkan telur orak-arik","Masukkan nasi + kecap + garam","Aduk rata sampai harum")
        );
    }

    public static Resep rAyamKecap() {
        return new Resep(
                "Ayam Kecap", "Lauk", 3, 35,
                bahan("300g ayam","3 sdm kecap","1 siung bawang putih","lada","garam","air"),
                langkah("Tumis bawang, masukkan ayam","Beri air + bumbu, masak hingga empuk","Tambahkan kecap, koreksi rasa")
        );
    }

    public static Resep rKangkung() {
        return new Resep(
                "Tumis Kangkung", "Sayur", 2, 12,
                bahan("1 ikat kangkung","2 siung bawang putih","cabai","garam","minyak"),
                langkah("Tumis bawang + cabai","Masukkan kangkung, bumbui garam","Masak cepat, sajikan")
        );
    }

    public static Resep rAglioOlio() {
        return new Resep(
                "Spaghetti Aglio Olio", "Pasta", 1, 15,
                bahan("100g spaghetti","2 siung bawang putih","minyak zaitun","cabai kering","garam"),
                langkah("Rebus spaghetti al dente","Tumis bawang + cabai","Masukkan spaghetti + garam, aduk rata")
        );
    }

    public static Resep rPancake() {
        return new Resep(
                "Pancake Sederhana", "Dessert", 2, 25,
                bahan("150g tepung","1 butir telur","200ml susu","1 sdm gula","1 sdt baking powder","mentega"),
                langkah("Campur bahan kering + basah","Panaskan wajan dan oles mentega","Tuang adonan, balik sekali")
        );
    }

    private static List<Bahan> bahan(String... s){
        List<Bahan> out = new ArrayList<>(s.length);
        Arrays.stream(s).forEach(x -> out.add(new Bahan(x)));
        return out;
    }
    private static List<Langkah> langkah(String... s){
        List<Langkah> out = new ArrayList<>(s.length);
        Arrays.stream(s).forEach(x -> out.add(new Langkah(x)));
        return out;
    }
}
