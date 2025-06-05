package kazikd.dev.server.Service;

import kazikd.dev.server.Model.News;
import kazikd.dev.server.Model.NewsDTO;
import kazikd.dev.server.Model.NewsResponseDTO;
import kazikd.dev.server.Repository.NewsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NewsService {
    private static final double INITIAL = 50000;
    private static final double MAX = 500000;
    private static final double INCREMENT = 50000;

    private final NewsRepo newsRepo;

    public NewsService(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    public NewsResponseDTO getLocalAndNearbyNews(Long cityId) {
        List<NewsDTO> localNews = newsRepo.findTop10ByCityIdOrderByPublishedAtDesc(cityId)
            .stream()
            .map(NewsDTO::fromNews)
            .toList();

        List<NewsDTO> nearbyNews = findNearbyNews(cityId);

        return new NewsResponseDTO(localNews, nearbyNews);
    }

    public List<NewsDTO> getGlobalNews() {
        return newsRepo.findTop10ByIsGlobalTrueOrderByPublishedAtDesc()
            .stream()
            .map(NewsDTO::fromNews)
            .toList();
    }

    private List<NewsDTO> findNearbyNews(Long cityId) {
        double current = INITIAL;
        List<News> nearbyNews;

        do {
            nearbyNews = newsRepo.findNearbyNews(cityId, current);
            if (!nearbyNews.isEmpty() || current >= MAX) {
                break;
            }
            current += INCREMENT;
        } while (true);

        return nearbyNews.stream()
            .limit(10)
            .map(NewsDTO::fromNews)
            .toList();
    }
} 