import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Statistics {
    private long totalTraffic = 0;
    private LocalDateTime minTime = new LocalDateTime("01/Jan/2900:00:00:00 +0300");
    private LocalDateTime maxTime = new LocalDateTime("01/Jan/1900:00:00:00 +0300");;
    private HashSet<String> pagePaths = new HashSet<>();
    private HashMap<String, Integer> opSystemStat = new HashMap<>();

    public Statistics() {
    }

    public void addEntry(LogEntry log){

        //Аккумулирование суммарного значения трафика
        totalTraffic+=log.getResponseSize();

        //Актуализация мин и макс дат
        if(log.getTimeFromLog().date.getTime() < this.minTime.date.getTime()){
            this.minTime=log.getTimeFromLog();
        }

        if(log.getTimeFromLog().date.getTime() > this.maxTime.date.getTime()){
            this.maxTime=log.getTimeFromLog();
        }

        //Добавление адресов в сет
        if(log.getResponseCode()==200){
            this.pagePaths.add(log.getPath());
        }

        //
        if(log.getAgent().getOperationalSystem() != null){
            if(this.opSystemStat.containsKey(log.getAgent().getOperationalSystem())){
                this.opSystemStat.put(log.getAgent().getOperationalSystem(), this.opSystemStat.get(log.getAgent().getOperationalSystem())+1);
            }else {
                this.opSystemStat.put(log.getAgent().getOperationalSystem(), 1);
            }
        }
    }

    public double getTrafficRate(){

        Instant minTime = this.minTime.date.toInstant();
        Instant maxTime = this.maxTime.date.toInstant();

        Duration duration = Duration.between(minTime, maxTime);
        long diffInHours = duration.toHours();

        return (double) this.totalTraffic/diffInHours;

    }

    public HashSet getPagePath(){
        return this.pagePaths;
    }

    public HashMap getOpSystemStat(){

        int tmp = 0;
        for (String key : this.opSystemStat.keySet()){
            tmp+=this.opSystemStat.get(key);
        }

        HashMap<String, Double> opSystemStat = new HashMap<>();

        for (String key : this.opSystemStat.keySet()){
            opSystemStat.put(key, (double)this.opSystemStat.get(key)/tmp);
        }

        return opSystemStat;
    }
}
