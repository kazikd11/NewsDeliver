package kazikd.dev.server.Model;

import java.util.List;

public record NewsApiResponse(int offset, int number, int available, List<NewsApiDTO> news) {
}
