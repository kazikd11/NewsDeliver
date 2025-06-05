package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepo extends JpaRepository<News, Integer> {
    List<News> findByIsGlobalFalseAndCityIsNull();
    
    List<News> findTop10ByCityIdOrderByPublishedAtDesc(Long cityId);

    @Query(value = """
        SELECT n FROM News n
        JOIN n.city c
        WHERE c.id != :cityId
        AND function('earth_distance',
            function('ll_to_earth',
                (SELECT c2.lat FROM City c2 WHERE c2.id = :cityId),
                (SELECT c2.lng FROM City c2 WHERE c2.id = :cityId)
            ),
            function('ll_to_earth', c.lat, c.lng)
        ) < :radius
        ORDER BY n.publishedAt DESC
        """)
    List<News> findNearbyNews(@Param("cityId") Long cityId, @Param("radius") double radius);

    List<News> findTop10ByIsGlobalTrueOrderByPublishedAtDesc();
}
