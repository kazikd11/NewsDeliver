package kazikd.dev.server.Service;

import kazikd.dev.server.Model.City;
import kazikd.dev.server.Model.News;
import kazikd.dev.server.Model.NewsApiDTO;
import kazikd.dev.server.Model.NewsApiResponse;
import kazikd.dev.server.Repository.CitiesRepo;
import kazikd.dev.server.Repository.NewsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class NewsFetchService {

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final CitiesRepo citiesRepo;
    private final NewsRepo newsRepo;

    public NewsFetchService(CitiesRepo citiesRepo, NewsRepo newsRepo) {
        this.restTemplate = new RestTemplate();
        this.citiesRepo = citiesRepo;
        this.newsRepo = newsRepo;
    }

    public void fetchAndSaveNews() {
        List<City> cities = citiesRepo.findTop1ByOrderByPopulation();

        for (City city : cities) {
            try{
                String url = UriComponentsBuilder
                        .fromUri(URI.create(apiUrl))
                        .queryParam("language", "en")
                        .queryParam("source-country", "us")
                        .queryParam("location-filter", city.getLat() + "," + city.getLng() + ",100")
                        .queryParam("sort", "publish-time")
                        .queryParam("sort-direction", "DESC")
                        .queryParam("number", 10)
                        .queryParam("api-key", apiKey)
                        .build()
                        .toUriString();

                ResponseEntity<NewsApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, null , NewsApiResponse.class);

                if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    for (NewsApiDTO item : response.getBody().news()) {
                        newsRepo.save(News.fromApiDTO(item));
                    }
                }

            }
            catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
}
