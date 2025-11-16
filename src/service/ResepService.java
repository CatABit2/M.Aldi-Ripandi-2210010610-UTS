package service;

import domain.Resep;
import repo.FileResepRepository;
import util.Samples;
import util.TxtResepIO;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResepService {
    private final FileResepRepository repo;
    private final List<Resep> cache = new ArrayList<>();

    public ResepService(FileResepRepository repo) {
        this.repo = repo;
        load();
        if (cache.isEmpty()) seed();
    }

    // ===== CRUD in-memory + persist =====
    public List<Resep> list() {
        return new ArrayList<>(cache);
    }

    public void add(Resep r) {
        cache.add(r);
        persistSilent();
    }

    public void update(int idx, Resep r) {
        cache.set(idx, r);
        persistSilent();
    }

    public void delete(int idx) {
        cache.remove(idx);
        persistSilent();
    }

    // ===== TXT Import/Export utk “tantangan 20 poin” =====
    public void importFromTxt(File f, boolean replace) throws Exception {
        List<Resep> incoming = TxtResepIO.read(f);
        if (replace) cache.clear();
        cache.addAll(incoming);
        repo.save(cache);
    }

    public void exportToTxt(File f) throws Exception {
        TxtResepIO.write(f, cache);
    }

    // ===== internal =====
    private void load() {
        try {
            cache.clear();
            cache.addAll(repo.load());
        } catch (Exception e) {
            cache.clear(); // file belum ada ya sudahlah
        }
    }

    private void persistSilent() {
        try { repo.save(cache); } catch (Exception ignored) {}
    }

    private void seed() {
        cache.addAll(Arrays.asList(
                Samples.rNasiGoreng(),
                Samples.rAyamKecap(),
                Samples.rKangkung(),
                Samples.rAglioOlio(),
                Samples.rPancake()
        ));
        persistSilent();
    }
}
