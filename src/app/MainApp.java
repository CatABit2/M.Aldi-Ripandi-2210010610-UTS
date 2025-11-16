// =======================
// File: src/app/MainApp.java
// =======================
package app;

import ui.ResepFrame;
import service.ResepService;
import repo.FileResepRepository;   // <— paketmu ‘repo’
import util.UtilsPath;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ResepService svc =
                    new ResepService(new FileResepRepository(UtilsPath.resepTxt()));
            new ResepFrame(svc).setVisible(true);
        });
    }
}
