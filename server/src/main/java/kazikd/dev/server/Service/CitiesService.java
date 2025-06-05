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
