package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitiesRepo extends JpaRepository<City, Long> {

    List<City> findByNameIgnoreCase(String q);

    List<City> findTop10ByNameStartingWithIgnoreCaseOrderByPopulationDesc(String q);

    List<City> findTop10ByNameContainingIgnoreCaseOrderByPopulationDesc(String q);

    @Query(
            value = """
        SELECT *, similarity(name, :q) * 0.8 + (population::float / (SELECT MAX(population) FROM cities)) * 0.2 AS score
        FROM cities
        WHERE similarity(name, :q) > 0.2
        ORDER BY score DESC
        LIMIT 10;
        """,
            nativeQuery = true
    )
    List<City> findBySimilarity(@Param("q") String q);
}
