package kazikd.dev.server.Model;

public record CityDTO(Long id, String name, String state) {
    public static CityDTO fromCity(City city) {
        return new CityDTO(city.getId(), city.getName(), city.getState());
    }
}
