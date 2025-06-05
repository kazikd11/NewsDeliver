package kazikd.dev.server.Controller;

import kazikd.dev.server.Model.NewsResponseDTO;
import kazikd.dev.server.Service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public ResponseEntity<NewsResponseDTO> getNews(@RequestParam Long cityId) {
        NewsResponseDTO news = newsService.getNews(cityId);
        return ResponseEntity.ok(news);
    }
} 