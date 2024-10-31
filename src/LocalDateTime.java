import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalDateTime {
    public Date date;
    public String dateStr;

    public LocalDateTime(String str) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

            Date date = formatter.parse(str);
            this.date = date;
            this.dateStr = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "LocalDateTime{" +
                "date=" + date +
                ", dateStr='" + dateStr + '\'' +
                '}';
    }
}
