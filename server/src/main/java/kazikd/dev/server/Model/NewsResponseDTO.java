package kazikd.dev.server.Model;

import java.util.List;

public record NewsResponseDTO(
    List<NewsDTO> localNews,
    List<NewsDTO> nearbyNews,
    List<NewsDTO> globalNews
) {}