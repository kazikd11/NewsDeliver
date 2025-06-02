package kazikd.dev.server.Init;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import kazikd.dev.server.Model.City;
import kazikd.dev.server.Repository.CitiesRepo;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

                System.out.println("Cities data loaded successfully: " + cities.size() + " records.");
                //to do add logger
            }
        }


    }
}
