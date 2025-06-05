package kazikd.dev.server.Model;

public record NewsDTO(
    Long id,
    String title,
    String summary,
    String imageUrl,
    String url,
    String publishedAt,
    CityDTO city
) {
    public static NewsDTO fromNews(News news) {
        return new NewsDTO(
            news.getId(),
            news.getTitle(),
            news.getSummary(),
            news.getImageUrl(),
            news.getUrl(),
            news.getPublishedAt().toString(),
            news.getCity() != null ? CityDTO.fromCity(news.getCity()) : null
        );
    }
} 