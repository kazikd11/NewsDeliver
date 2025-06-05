package kazikd.dev.server.Controller;

import kazikd.dev.server.Model.NewsResponseDTO;
import kazikd.dev.server.Model.NewsDTO;
import kazikd.dev.server.Service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news/local")
    public ResponseEntity<NewsResponseDTO> getLocalNews(@RequestParam Long cityId) {
        NewsResponseDTO news = newsService.getLocalAndNearbyNews(cityId);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/news/global")
    public ResponseEntity<List<NewsDTO>> getGlobalNews() {
        List<NewsDTO> globalNews = newsService.getGlobalNews();
        return ResponseEntity.ok(globalNews);
    }
} 