// =======================
// File: src/ui/ResepDialog.java
// =======================
package ui;

import domain.Resep;
import domain.Bahan;
import domain.Langkah;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ResepDialog extends JDialog {
    private final JTextField tfJudul = new JTextField();
    private final JTextField tfKategori = new JTextField();
    private final JSpinner spPorsi = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    private final JSpinner spDurasi = new JSpinner(new SpinnerNumberModel(5, 1, 1440, 1));
    private final JTextArea taBahan = new JTextArea(6, 30);
    private final JTextArea taLangkah = new JTextArea(6, 30);

    private boolean ok = false;
    private Resep edited;

    public ResepDialog(Frame owner, Resep current) {
        super(owner, "Form Resep", true);
        this.edited = current;

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setLayout(new BorderLayout(0,0));
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(new EmptyBorder(12,12,12,12));
        add(root, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0;

        form.add(new JLabel("Judul"), g);
        g.gridx = 1; g.weightx = 1; form.add(tfJudul, g);

        g.gridy++; g.gridx = 0; g.weightx = 0; form.add(new JLabel("Kategori"), g);
        g.gridx = 1; g.weightx = 1; form.add(tfKategori, g);

        g.gridy++; g.gridx = 0; g.weightx = 0; form.add(new JLabel("Porsi"), g);
        g.gridx = 1; g.weightx = 1; form.add(spPorsi, g);

        g.gridy++; g.gridx = 0; g.weightx = 0; form.add(new JLabel("Durasi (menit)"), g);
        g.gridx = 1; g.weightx = 1; form.add(spDurasi, g);

        g.gridy++; g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE; form.add(new JLabel("Bahan (1/baris)"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.BOTH; form.add(new JScrollPane(taBahan), g);

        g.gridy++; g.gridx = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE; form.add(new JLabel("Langkah (1/baris)"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.BOTH; form.add(new JScrollPane(taLangkah), g);

        root.add(form, BorderLayout.CENTER);

        JPanel act = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = new JButton("Simpan");
        JButton bCancel = new JButton("Batal");
        act.add(bOk); act.add(bCancel);
        root.add(act, BorderLayout.SOUTH);

        // Prefill kalau edit
        if (current != null) {
            tfJudul.setText(ns(current.getJudul()));
            tfKategori.setText(ns(current.getKategori()));
            spPorsi.setValue(current.getPorsi() <= 0 ? 1 : current.getPorsi());
            spDurasi.setValue(current.getDurasiMenit() <= 0 ? 5 : current.getDurasiMenit());
            taBahan.setText(joinBahan(current.getBahan()));
            taLangkah.setText(joinLangkah(current.getLangkah()));
        }

        // Actions
        bOk.addActionListener(e -> onOk());
        bCancel.addActionListener(e -> { ok = false; dispose(); });

        // Enter = OK, Esc = Cancel
        getRootPane().setDefaultButton(bOk);
        getRootPane().registerKeyboardAction(
                (ActionEvent e) -> { ok = false; dispose(); },
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        setSize(560, 560);
        setLocationRelativeTo(owner);
    }

    private void onOk() {
        String judul = tfJudul.getText().trim();
        if (judul.isEmpty()) { warn("Judul wajib diisi."); tfJudul.requestFocus(); return; }

        String kategori = tfKategori.getText().trim();
        if (kategori.isEmpty()) { warn("Kategori wajib diisi."); tfKategori.requestFocus(); return; }

        int porsi = (Integer) spPorsi.getValue();
        if (porsi <= 0) { warn("Porsi harus > 0."); spPorsi.requestFocus(); return; }

        int durasi = (Integer) spDurasi.getValue();
        if (durasi <= 0) { warn("Durasi harus > 0."); spDurasi.requestFocus(); return; }

        List<Bahan> bahan = toBahan(taBahan.getText());
        if (bahan.isEmpty()) { warn("Minimal 1 baris bahan."); taBahan.requestFocus(); return; }

        List<Langkah> langkah = toLangkah(taLangkah.getText());
        if (langkah.isEmpty()) { warn("Minimal 1 langkah."); taLangkah.requestFocus(); return; }

        if (edited == null) {
            edited = new Resep(judul, kategori, porsi, durasi, bahan, langkah);
        } else {
            edited.setJudul(judul);
            edited.setKategori(kategori);
            edited.setPorsi(porsi);
            edited.setDurasiMenit(durasi);
            edited.setBahan(bahan);
            edited.setLangkah(langkah);
        }
        ok = true;
        dispose();
    }

    private static List<Bahan> toBahan(String txt){
        List<Bahan> out = new ArrayList<>();
        if (txt == null) return out;
        for (String s : txt.split("\\r?\\n")) {
            String t = s.trim();
            if (!t.isEmpty()) out.add(new Bahan(t));
        }
        return out;
    }

    private static List<Langkah> toLangkah(String txt){
        List<Langkah> out = new ArrayList<>();
        if (txt == null) return out;
        for (String s : txt.split("\\r?\\n")) {
            String t = s.trim();
            if (!t.isEmpty()) out.add(new Langkah(t));
        }
        return out;
    }

    private static String joinBahan(List<Bahan> ls){
        StringBuilder sb = new StringBuilder();
        if (ls != null) for (int i=0;i<ls.size();i++){
            if (i>0) sb.append('\n');
            Bahan b = ls.get(i);
            sb.append(b==null?"":ns(b.getTeks()));
        }
        return sb.toString();
    }

    private static String joinLangkah(List<Langkah> ls){
        StringBuilder sb = new StringBuilder();
        if (ls != null) for (int i=0;i<ls.size();i++){
            if (i>0) sb.append('\n');
            Langkah l = ls.get(i);
            sb.append(l==null?"":ns(l.getTeks()));
        }
        return sb.toString();
    }

    private static void warn(String m) { JOptionPane.showMessageDialog(null, m, "Validasi", JOptionPane.WARNING_MESSAGE); }
    private static String ns(String s) { return s==null?"":s; }

    public boolean isOk() { return ok; }
    public Resep getValue() { return edited; }
}
