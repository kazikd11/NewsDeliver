package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepo extends JpaRepository<News, Integer> {
    List<News> findByIsGlobalFalseAndCityIsNull();
}
