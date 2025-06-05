package kazikd.dev.server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record NewsApiDTO(
        long id,
        String title,
        String summary,
        String url,
        String image,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime publish_date
){}