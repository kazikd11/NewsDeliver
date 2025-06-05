package kazikd.dev.server.Repository;

import kazikd.dev.server.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepo extends JpaRepository<News, Integer> {
}
