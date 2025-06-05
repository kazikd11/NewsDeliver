package kazikd.dev.server.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String summary;

    private String imageUrl;
    private String url;

    private LocalDateTime publishedAt;

    private boolean isGlobal;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public static News fromApiDTO(NewsApiDTO dto) {
        News news = new News();
        news.setTitle(dto.title());
        news.setSummary(dto.summary());
        news.setUrl(dto.url());
        news.setImageUrl(dto.image());
        news.setPublishedAt(dto.publish_date());
        return news;
    }
}
