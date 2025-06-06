package kazikd.dev.server.Schedule;

import kazikd.dev.server.Service.NewsFetchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduleFetchNews {

    @Value("${news.fetch.schedule}")
    private boolean fetchSchedule;

    private final NewsFetchService newsFetchService;

    public ScheduleFetchNews(NewsFetchService newsFetchService) {
        this.newsFetchService = newsFetchService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchNews() {
        if(!fetchSchedule) return;
        log.info("Scheduled fetching news");
        newsFetchService.fetchAndSaveNews();
    }
}
