package kazikd.dev.server.Service;

import kazikd.dev.server.Model.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OpenAIService {

    private final ChatClient chatClient;

    public OpenAIService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public Optional<Location> analyzeNewsLocation(News news) {
        try {
            String result = chatClient.prompt()
                .system("""
                    You are a news location analyzer. Your task is to determine if a news article is about a specific US city
                    or if it's a global/national news story. If it's about a specific city, identify the city and state.
                    Consider the context carefully - for example, 'Washington' could refer to different cities.
                    Respond ONLY with:
                    GLOBAL if it's a global/national news
                    or
                    {city_name},{state_code} if it's about a specific city (use 2-letter state code)
                    """)
                .user("Title: " + news.getTitle() + "\nSummary: " + news.getSummary())
                .call()
                .content();

            if(result == null || result.isEmpty()) {
                log.error("Received empty response from OpenAI for: {}", news.getId());
                return Optional.empty();
            }

            String r = result.trim();
            
            if ("GLOBAL".equals(r)) {
                return Optional.of(new Location(true, null, null));
            }

            String[] parts = r.split(",");
            if (parts.length == 2) {
                String cityName = parts[0].trim();
                String stateCode = parts[1].trim();
                return Optional.of(new Location(false, cityName, stateCode));
            }

            log.error("Unexpected response format from OpenAI for news ID {}: {}", news.getId(), result);
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error analyzing news location for news ID {}: {}", news.getId(), e.getMessage());
            return Optional.empty();
        }
    }

    public record Location(boolean isGlobal, String name, String state) {}
} 