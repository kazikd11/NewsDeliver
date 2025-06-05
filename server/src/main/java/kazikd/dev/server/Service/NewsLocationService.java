package kazikd.dev.server.Service;

import kazikd.dev.server.Model.City;
import kazikd.dev.server.Model.News;
import kazikd.dev.server.Repository.CitiesRepo;
import kazikd.dev.server.Repository.NewsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NewsLocationService {

    private final NewsRepo newsRepo;
    private final CitiesRepo citiesRepo;
    private final OpenAIService openAIService;

    public NewsLocationService(NewsRepo newsRepo, CitiesRepo citiesRepo, OpenAIService openAIService) {
        this.newsRepo = newsRepo;
        this.citiesRepo = citiesRepo;
        this.openAIService = openAIService;
    }

    @Transactional
    public void analyzeUnassignedNews() {
        List<News> unassignedNews = newsRepo.findByIsGlobalFalseAndCityIsNull();
        log.info("Found {} unassigned news to analyze", unassignedNews.size());

        for (News news : unassignedNews) {

            Optional<OpenAIService.Location> result = openAIService.analyzeNewsLocation(news);

            if (result.isPresent()) {
                OpenAIService.Location location = result.get();

                if (location.isGlobal()) {
                    news.setGlobal(true);
                } else {
                    Optional<City> city = citiesRepo.findByNameIgnoreCaseAndState(
                            location.name(),
                            location.state()
                    );

                    if (city.isPresent()) {
                        news.setCity(city.get());
                    } else {
                        news.setGlobal(true);
                        log.warn("Could not find city {}, {} in database", location.name(), location.state());
                    }
                }

                newsRepo.save(news);
                log.info("Successfully analyzed news: {}", news.getId());
            }
        }
    }
}