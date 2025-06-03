package kazikd.dev.server.Service;

import kazikd.dev.server.Model.City;
import kazikd.dev.server.Model.CityDTO;
import kazikd.dev.server.Repository.CitiesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CitiesService {

    private final CitiesRepo citiesRepo;

    public CitiesService(CitiesRepo citiesRepo) {
        this.citiesRepo = citiesRepo;
    }

    public List<City> findNearbyCities(Long cityId, double radius) {
        City city = citiesRepo.findById(cityId)
                .orElseThrow(() -> {
                            log.error("City with ID {} not found", cityId);
                            return new IllegalArgumentException("City not found");
                        }
                );

        return citiesRepo.findNearbyCities(cityId, city.getLat(), city.getLng(), radius * 1000)
                .stream().filter(c -> !c.getId().equals(cityId)).collect(Collectors.toList());
    }

    public List<CityDTO> findMatching(String namePart) {

        List<City> result = new ArrayList<>(citiesRepo.findByNameIgnoreCase(namePart));

        Set<Long> ids = result.stream()
                .map(City::getId)
                .collect(Collectors.toSet());

        if (result.size() < 10) {
            for (City city : citiesRepo.findTop10ByNameStartingWithIgnoreCaseOrderByPopulationDesc(namePart)) {
                if (ids.add(city.getId())) result.add(city);
                if (result.size() == 10) break;
            }
        }

        if (result.size() < 10) {
            for (City city : citiesRepo.findTop10ByNameContainingIgnoreCaseOrderByPopulationDesc(namePart)) {
                if (ids.add(city.getId())) result.add(city);
                if (result.size() == 10) break;
            }
        }

        if (result.size() < 10) {
            for (City city : citiesRepo.findBySimilarity(namePart)) {
                if (ids.add(city.getId())) result.add(city);
                if (result.size() == 10) break;
            }
        }

        return result.stream().map(CityDTO::fromCity).collect(Collectors.toList());
    }
}
