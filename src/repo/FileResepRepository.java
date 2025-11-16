package repo;

import domain.Resep;
import util.TxtResepIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileResepRepository {
    private final File file;

    public FileResepRepository(File file) {
        this.file = file;
    }

    public FileResepRepository(String path) {
        this(new File(path));
    }

    public List<Resep> load() throws Exception {
        if (!file.exists()) return new ArrayList<>();
        return TxtResepIO.read(file);
    }

    public void save(List<Resep> list) throws Exception {
        if (file.getParentFile() != null) file.getParentFile().mkdirs();
        TxtResepIO.write(file, list);
    }

    public File getFile() { return file; }
}
