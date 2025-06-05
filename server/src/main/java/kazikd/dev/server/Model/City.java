package kazikd.dev.server.Model;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "city_ascii")
    private String name;
    @CsvBindByName(column = "state_id")
    private String state;

    @CsvBindByName(column = "population")
    private Long population;

    @CsvBindByName(column = "lat")
    private Double lat;
    @CsvBindByName(column = "lng")
    private Double lng;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<News> news;

}
