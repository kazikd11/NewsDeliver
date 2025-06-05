package kazikd.dev.server.Schedule;

import kazikd.dev.server.Service.NewsLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduleLocationAnalysis {

    @Value("${news.location.schedule}")
    private boolean locationSchedule;

    private final NewsLocationService newsLocationService;

    public ScheduleLocationAnalysis(NewsLocationService newsLocationService) {
        this.newsLocationService = newsLocationService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void analyzeLocations() {
        if (!locationSchedule) return;
        log.info("Scheduled location analysis");
        newsLocationService.analyzeUnassignedNews();
    }
} 