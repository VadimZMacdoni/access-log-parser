import java.time.Duration;
import java.time.Instant;

public class Statistics {
    private int totalTraffic = 0;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
    }

    public void addEntry(LogEntry log){
        totalTraffic+=log.getResponseSize();
        if(log.getTime().date.getTime() < this.minTime.date.getTime()){
            this.minTime=log.getTime();
        }
        if(log.getTime().date.getTime() > this.maxTime.date.getTime()){
            this.maxTime=log.getTime();
        }
    }

    public double getTrafficRate(){
        Instant minTime = this.minTime.date.toInstant();
        Instant maxTime = this.maxTime.date.toInstant();

        Duration duration = Duration.between(minTime, maxTime);
        long diffInHours = duration.toHours();

        return (double) this.totalTraffic/diffInHours;

    }
}
