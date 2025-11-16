// =======================
// File: src/ui/ResepFrame.java
// =======================
package ui;

import service.ResepService;
import domain.Resep;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;

public class ResepFrame extends JFrame {
    private final ResepService service;
    private final ResepTableModel model;
    private final JTable table;
    private final JTextField tfCari = new JTextField();

public ResepFrame(ResepService service) {
    super("Aplikasi Resep Masakan – UTS");
    this.service = service;
    this.model = new ResepTableModel(service.list());
    this.table = new JTable(model);

    // PILIH NIMBUS JIKA ADA, JANGAN DITIMPA LAGI
    try {
        boolean ok = false;
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                ok = true; break;
            }
        }
        if (!ok) UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) {}

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(14,16,12,16));
        header.setBackground(new Color(248,250,252));

        JLabel title = new JLabel("Resep Masakan");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setForeground(new Color(33, 43, 54));

        JPanel search = new JPanel(new BorderLayout(8,8));
        search.setOpaque(false);
        JLabel lbCari = new JLabel("Cari:");
        lbCari.setBorder(new EmptyBorder(0,0,0,6));
        tfCari.putClientProperty("JTextField.placeholderText", "judul / kategori…");
        tfCari.setMargin(new Insets(4,8,4,8));
        search.add(lbCari, BorderLayout.WEST);
        search.add(tfCari, BorderLayout.CENTER);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topRight.setOpaque(false);
        ReadableButton bImport = priButton("Import TXT");
        ReadableButton bExport = priButton("Export TXT");
        ReadableButton bReload = secButton("Reload");
        ReadableButton bAdd    = priButton("Tambah");
        ReadableButton bEdit   = secButton("Ubah");
        ReadableButton bDel    = dangerButton("Hapus");
        topRight.add(bImport); topRight.add(bExport); topRight.add(bReload);
        topRight.add(new JSeparator(SwingConstants.VERTICAL));
        topRight.add(bAdd); topRight.add(bEdit); topRight.add(bDel);

        header.add(title, BorderLayout.WEST);
        header.add(search, BorderLayout.CENTER);
        header.add(topRight, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== Tabel =====
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        zebra(table);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));

        TableRowSorter<ResepTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        setColsWidth(table, 60, 260, 160, 80, 110);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);

        // ===== Events =====
        tfCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String q = tfCari.getText().trim();
                sorter.setRowFilter(q.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(q)));
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        bAdd.addActionListener(e -> onAdd());
        bEdit.addActionListener(e -> onEdit());
        bDel.addActionListener(e -> onDel());
        bReload.addActionListener(e -> onReload());
        bImport.addActionListener(e -> onImportTxt());
        bExport.addActionListener(e -> onExportTxt());

        // contoh: kalau kamu mau nonaktifkan dulu sebagian tombol
        // bAdd.setEnabled(false); bDel.setEnabled(false);  // teksnya tetap kebaca karena custom button

        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    private int selectedModelRow() {
        int v = table.getSelectedRow();
        return v < 0 ? -1 : table.convertRowIndexToModel(v);
    }

    private void onAdd() {
        ResepDialog dlg = new ResepDialog(this, null);
        dlg.setVisible(true);
        if (dlg.isOk()) {
            service.add(dlg.getValue());
            model.setData(service.list());
        }
    }

    private void onEdit() {
        int idx = selectedModelRow();
        if (idx < 0) { JOptionPane.showMessageDialog(this, "Pilih baris dulu."); return; }
        Resep current = model.getAt(idx);
        ResepDialog dlg = new ResepDialog(this, current);
        dlg.setVisible(true);
        if (dlg.isOk()) {
            service.update(idx, dlg.getValue());
            model.setData(service.list());
        }
    }

    private void onDel() {
        int idx = selectedModelRow();
        if (idx < 0) { JOptionPane.showMessageDialog(this, "Pilih baris dulu."); return; }
        if (JOptionPane.showConfirmDialog(this, "Hapus resep terpilih?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            service.delete(idx);
            model.setData(service.list());
        }
    }

    private void onReload() {
        model.setData(service.list());
        JOptionPane.showMessageDialog(this, "Data dimuat ulang.");
    }

    private void onImportTxt() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            Object[] opts = {"Timpa semua", "Tambahkan", "Batal"};
            int c = JOptionPane.showOptionDialog(this, "Impor bagaimana?",
                    "Impor TXT", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opts, opts[0]);
            if (c==2 || c==-1) return;
            boolean replace = (c==0);
            try {
                service.importFromTxt(f, replace);
                model.setData(service.list());
                JOptionPane.showMessageDialog(this, "Impor TXT sukses.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal impor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExportTxt() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("resep.txt"));
        if (fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                service.exportToTxt(fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Export TXT sukses.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal export: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== Custom button yg teksnya tetap kebaca saat disabled =====
    private static class ReadableButton extends JButton {
        private final Color baseBg, baseFg, hoverBg, disabledBg, disabledFg;

        ReadableButton(String text, Color baseBg, Color baseFg, Color hoverBg,
                       Color disabledBg, Color disabledFg) {
            super(text);
            this.baseBg = baseBg;
            this.baseFg = baseFg;
            this.hoverBg = hoverBg;
            this.disabledBg = disabledBg;
            this.disabledFg = disabledFg;

            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(8,14,8,14));
            setOpaque(true);
            setContentAreaFilled(true);
            setBackground(baseBg);
            setForeground(baseFg);

            addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseEntered(java.awt.event.MouseEvent e){
                    if (isEnabled()) setBackground(hoverBg);
                }
                public void mouseExited(java.awt.event.MouseEvent e){
                    if (isEnabled()) setBackground(baseBg);
                }
            });
        }

        @Override public void setEnabled(boolean b) {
            super.setEnabled(b);
            if (b) {
                setBackground(baseBg);
                setForeground(baseFg);
            } else {
                setBackground(disabledBg);
                setForeground(disabledFg); // ini yang bikin teks tetap kontras
            }
        }
    }

    // ===== Factory tombol =====
    private static ReadableButton priButton(String t){
        return new ReadableButton(
                t,
                new Color(30,136,229), Color.WHITE,
                new Color(25,118,210),
                new Color(235,240,246), new Color(70,80,90)
        );
    }
    private static ReadableButton secButton(String t){
        return new ReadableButton(
                t,
                new Color(240,243,247), new Color(33,43,54),
                new Color(230,235,240),
                new Color(245,246,248), new Color(100,110,120)
        );
    }
    private static ReadableButton dangerButton(String t){
        return new ReadableButton(
                t,
                new Color(220,53,69), Color.WHITE,
                new Color(200,40,55),
                new Color(245,228,230), new Color(120,40,45)
        );
    }

    // ===== Tabel helpers =====
    private static void zebra(JTable t){
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected){
                    c.setBackground(row%2==0 ? Color.WHITE : new Color(248,250,252));
                }
                if (column==0 || column==3 || column==4) setHorizontalAlignment(RIGHT);
                else setHorizontalAlignment(LEFT);
                return c;
            }
        });
        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setFont(h.getFont().deriveFont(Font.BOLD));
    }

    private static void setColsWidth(JTable t, int... w){
        TableColumnModel m = t.getColumnModel();
        for (int i=0;i<w.length && i<m.getColumnCount();i++){
            TableColumn c = m.getColumn(i);
            c.setPreferredWidth(w[i]);
        }
    }
}
