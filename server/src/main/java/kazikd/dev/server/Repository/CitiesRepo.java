package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitiesRepo extends JpaRepository<City, Long> {
    List<City> findTop10ByNameIgnoreCaseContaining(String namePart);
}
