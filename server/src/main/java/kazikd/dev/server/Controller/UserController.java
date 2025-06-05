package kazikd.dev.server.Controller;

import kazikd.dev.server.Model.CityDTO;
import kazikd.dev.server.Service.CitiesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final CitiesService citiesService;

    public UserController(CitiesService citiesService) {
        this.citiesService = citiesService;
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityDTO>> getCities(@RequestParam String namePart){
        List<CityDTO> data = citiesService.findMatching(namePart);
        return ResponseEntity.ok(data);
    }
}