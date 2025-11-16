// =======================
// File: src/repository/ResepRepository.java
// =======================
package repo;

import domain.Resep;
import java.io.IOException;
import java.util.List;

public interface ResepRepository {
    List<Resep> load() throws IOException;
    void save(List<Resep> data) throws IOException;
}
