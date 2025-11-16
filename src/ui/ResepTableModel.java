// =======================
// File: src/ui/ResepTableModel.java
// =======================
package ui;

import domain.Resep;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ResepTableModel extends AbstractTableModel {
    private final String[] cols = {"No", "Judul", "Kategori", "Porsi", "Waktu (mnt)"};
    private final List<Resep> data = new ArrayList<Resep>();

    public ResepTableModel() {}
    public ResepTableModel(List<Resep> list) { setData(list); }

    public void setData(List<Resep> list){
        data.clear();
        if (list != null) data.addAll(list);
        fireTableDataChanged();
    }

    public Resep getAt(int row){
        return (row < 0 || row >= data.size()) ? null : data.get(row);
    }

    @Override public int getRowCount(){ return data.size(); }
    @Override public int getColumnCount(){ return cols.length; }
    @Override public String getColumnName(int col){ return cols[col]; }

    @Override
    public Object getValueAt(int r, int c){
        Resep x = data.get(r);
        switch (c) {
            case 0: return r + 1;
            case 1: return nz(x.getJudul());
            case 2: return nz(x.getKategori());
            case 3: return Integer.valueOf(x.getPorsi());
            case 4: return Integer.valueOf(x.getDurasiMenit());
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int c){
        switch (c) {
            case 0:
            case 3:
            case 4:
                return Integer.class;
            default:
                return String.class;
        }
    }

    private static String nz(String s){ return s == null ? "" : s; }
}
