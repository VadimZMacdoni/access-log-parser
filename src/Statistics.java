import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class Statistics {
    private long totalTraffic = 0;
    private long numberUserVisits = 0;
    private long numberErrorQuery = 0;
    private HashSet<String> uniqueIpAdd = new HashSet<>();
    private LocalDateTime minTime = new LocalDateTime("01/Jan/2900:00:00:00 +0300");
    private LocalDateTime maxTime = new LocalDateTime("01/Jan/1900:00:00:00 +0300");;
    private HashSet<String> existingPages = new HashSet<>();
    private HashSet<String> notExistingPages = new HashSet<>();
    private HashMap<String, Integer> opSystemsStat = new HashMap<>();
    private HashMap<String, Integer> browsersStat = new HashMap<>();

    public Statistics() {
    }

    public void addEntry(LogEntry log){

        //Аккумулирование суммарного значения трафика
        this.totalTraffic+=log.getResponseSize();

        //Аккумулирование суммарного количества посещений
        if(!log.getAgent().getIsBot()) this.numberUserVisits++;

        //Аккумулирование запросов с ошибочным ответом
        if(log.getResponseCode()>=400) this.numberErrorQuery++;

        if(log.getIpAddr()!=null) this.uniqueIpAdd.add(log.getIpAddr());

        //Актуализация мин и макс дат
        if(log.getTimeFromLog().date.getTime() < this.minTime.date.getTime()){
            this.minTime=log.getTimeFromLog();
        }

        if(log.getTimeFromLog().date.getTime() > this.maxTime.date.getTime()){
            this.maxTime=log.getTimeFromLog();
        }

        //Добавление существующих адресов в сет
        if(log.getResponseCode()==200){
            this.existingPages.add(log.getPath());
        }

        //Добавление несуществующих адресов в сет
        if(log.getResponseCode()==404){
            this.notExistingPages.add(log.getPath());
        }

        //
        if(log.getAgent().getOperationalSystem() != null){
            if(this.opSystemsStat.containsKey(log.getAgent().getOperationalSystem())){
                this.opSystemsStat.put(log.getAgent().getOperationalSystem(), this.opSystemsStat.get(log.getAgent().getOperationalSystem())+1);
            }else {
                this.opSystemsStat.put(log.getAgent().getOperationalSystem(), 1);
            }
        }

        //
        if(log.getAgent().getBrowser() != null){
            if(this.browsersStat.containsKey(log.getAgent().getBrowser())){
                this.browsersStat.put(log.getAgent().getBrowser(), this.browsersStat.get(log.getAgent().getBrowser())+1);
            }else {
                this.browsersStat.put(log.getAgent().getBrowser(), 1);
            }
        }
    }

    public double getTrafficRate(){

        return (double) this.totalTraffic/this.getDiffInHours();

    }

    public double getAvgNumberVisits(){

        return (double) this.numberUserVisits/this.getDiffInHours();

    }

    public double getAvgNumberErrorQuery(){

        return (double) this.numberErrorQuery/this.getDiffInHours();

    }

    public double getAvgNumberVisitsByUser(){

        return (double) this.uniqueIpAdd.size()/this.numberUserVisits;

    }

    public long getDiffInHours(){

        Instant minTime = this.minTime.date.toInstant();
        Instant maxTime = this.maxTime.date.toInstant();

        Duration duration = Duration.between(minTime, maxTime);
        long diffInHours = duration.toHours();

        return diffInHours;

    }

    public HashSet getExistingPages(){
        return this.existingPages;
    }

    public HashSet getNotExistingPages(){
        return this.notExistingPages;
    }

    public HashMap getOpSystemsStat(){

        int tmp = 0;
        for (String key : this.opSystemsStat.keySet()){
            tmp+=this.opSystemsStat.get(key);
        }

        HashMap<String, Double> opSystemsStat = new HashMap<>();

        for (String key : this.opSystemsStat.keySet()){
            opSystemsStat.put(key, (double)this.opSystemsStat.get(key)/tmp);
        }

        return opSystemsStat;
    }

    public HashMap getBrowsersStat(){

        int tmp = 0;
        for (String key : this.browsersStat.keySet()){
            tmp+=this.browsersStat.get(key);
        }

        HashMap<String, Double> browsersStat = new HashMap<>();

        for (String key : this.browsersStat.keySet()){
            browsersStat.put(key, (double)this.browsersStat.get(key)/tmp);
        }

        return browsersStat;
    }
}
