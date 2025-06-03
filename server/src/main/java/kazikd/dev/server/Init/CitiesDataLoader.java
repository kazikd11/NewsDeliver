package kazikd.dev.server.Init;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import kazikd.dev.server.Model.City;
import kazikd.dev.server.Repository.CitiesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
public class CitiesDataLoader {

    private final CitiesRepo citiesRepo;

    public CitiesDataLoader(CitiesRepo citiesRepo) {
        this.citiesRepo = citiesRepo;
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
}
