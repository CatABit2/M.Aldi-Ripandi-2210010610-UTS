// =======================
// File: src/ui/ResepViewDialog.java
// =======================
package ui;

import domain.Bahan;
import domain.Langkah;
import domain.Resep;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ResepViewDialog extends JDialog {

    public ResepViewDialog(Frame owner, Resep r) {
        super(owner, "Detail Resep", true);
        if (r == null) throw new IllegalArgumentException("Resep null");

        setLayout(new BorderLayout(0,0));
        JPanel root = new JPanel(new BorderLayout(10,10));
        root.setBorder(new EmptyBorder(12,12,12,12));
        add(root, BorderLayout.CENTER);

        // header
        JLabel lbJudul = new JLabel(ns(r.getJudul()));
        lbJudul.setFont(lbJudul.getFont().deriveFont(Font.BOLD, 20f));

        JLabel lbMeta = new JLabel(
                "Kategori: " + ns(r.getKategori())
                        + "    |    Porsi: " + r.getPorsi()
                        + "    |    Waktu: " + r.getDurasiMenit() + " menit"
        );

        JPanel header = new JPanel(new BorderLayout(4,4));
        header.add(lbJudul, BorderLayout.NORTH);
        header.add(lbMeta, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        // isi
        JTextArea taBahan  = makeArea(joinBahan(r.getBahan()));
        JTextArea taLangkah = makeArea(joinLangkah(r.getLangkah()));

        JPanel center = new JPanel(new GridLayout(1,2,10,0));

        JPanel pBahan = new JPanel(new BorderLayout(4,4));
        pBahan.add(new JLabel("Bahan:"), BorderLayout.NORTH);
        pBahan.add(new JScrollPane(taBahan), BorderLayout.CENTER);

        JPanel pLangkah = new JPanel(new BorderLayout(4,4));
        pLangkah.add(new JLabel("Langkah:"), BorderLayout.NORTH);
        pLangkah.add(new JScrollPane(taLangkah), BorderLayout.CENTER);

        center.add(pBahan);
        center.add(pLangkah);
        root.add(center, BorderLayout.CENTER);

        // tombol
        JButton bTutup = new JButton("Tutup");
        bTutup.addActionListener(e -> dispose());
        JPanel act = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        act.add(bTutup);
        root.add(act, BorderLayout.SOUTH);

        setSize(700, 500);
        setLocationRelativeTo(owner);
    }

    private static JTextArea makeArea(String txt){
        JTextArea a = new JTextArea(txt);
        a.setEditable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setMargin(new Insets(4,4,4,4));
        return a;
    }

    private static String joinBahan(List<Bahan> ls){
        StringBuilder sb = new StringBuilder();
        if (ls != null) {
            for (int i=0;i<ls.size();i++){
                if (i>0) sb.append('\n');
                Bahan b = ls.get(i);
                sb.append(b == null ? "" : ns(b.getTeks()));
            }
        }
        return sb.toString();
    }

    private static String joinLangkah(List<Langkah> ls){
        StringBuilder sb = new StringBuilder();
        if (ls != null) {
            for (int i=0;i<ls.size();i++){
                if (i>0) sb.append('\n');
                Langkah l = ls.get(i);
                sb.append(l == null ? "" : ns(l.getTeks()));
            }
        }
        return sb.toString();
    }

    private static String ns(String s){ return s==null?"":s; }
}
