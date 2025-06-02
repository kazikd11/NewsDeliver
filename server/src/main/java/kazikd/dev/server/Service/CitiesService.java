package kazikd.dev.server.Service;

import kazikd.dev.server.Model.City;
import kazikd.dev.server.Repository.CitiesRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitiesService {

    private final CitiesRepo citiesRepo;

    public CitiesService(CitiesRepo citiesRepo) {
        this.citiesRepo = citiesRepo;
    }

    public List<City> findMatching(String namePart){
        return citiesRepo.findTop10ByNameIgnoreCaseContaining(namePart);
    }
}
