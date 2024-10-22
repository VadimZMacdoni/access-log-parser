import java.time.Duration;
import java.time.Instant;

public class Statistics {
    private int totalTraffic = 0;
    private LocalDateTime minTime = new LocalDateTime("01/Jan/2900:00:00:00 +0300");
    private LocalDateTime maxTime = new LocalDateTime("01/Jan/1900:00:00:00 +0300");;

    public Statistics() {
    }

    public void addEntry(LogEntry log){
        totalTraffic+=log.getResponseSize();
        if(log.getTimeFromLog().date.getTime() < this.minTime.date.getTime()){
            this.minTime=log.getTimeFromLog();
        }
        if(log.getTimeFromLog().date.getTime() > this.maxTime.date.getTime()){
            this.maxTime=log.getTimeFromLog();
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
