package kazikd.dev.server.Init;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import kazikd.dev.server.Model.City;
import kazikd.dev.server.Repository.CitiesRepo;
import kazikd.dev.server.Service.NewsFetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
public class DataLoader {

    @Value("${news.fetch.onstart}")
    private boolean fetchNewsOnStart;

    private final CitiesRepo citiesRepo;
    private final NewsFetchService newsFetchService;

    public DataLoader(CitiesRepo citiesRepo, NewsFetchService newsFetchService) {
        this.citiesRepo = citiesRepo;
        this.newsFetchService = newsFetchService;
    }

    @PostConstruct
    public void loadCitiesData() throws IOException{
        if(citiesRepo.count()>0) return;
        try (InputStream is = getClass().getResourceAsStream("/data/uscities.csv")){
            if (is == null) {
                throw new FileNotFoundException("Data file not found");
            }
            try (Reader reader = new InputStreamReader(is)){
                List<City> cities = new CsvToBeanBuilder<City>(reader)
                        .withType(City.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build()
                        .parse();

                citiesRepo.saveAll(cities);

                log.info("Cities data loaded successfully: {} records.", cities.size());
            }
        }
    }

    @PostConstruct
    public void debugFetchNews(){
        if(!fetchNewsOnStart) return;
        log.info("Fetching news on application start");
        newsFetchService.fetchAndSaveNews();
    }

}
