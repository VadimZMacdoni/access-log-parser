import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent agent;

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTimeFromLog() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getAgent() {
        return agent;
    }

    public LogEntry(String str) {

        // ip address
        this.ipAddr = extractFromLineGroup(str, "((?<!\\d)(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(?:\\.(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)){3}(?!\\d))[\\s]");

        // local date time
        LocalDateTime time = new LocalDateTime(extractFromLineGroup(str, "[\\s][\\[](.*?)[]]"));
        this.time = time;

        // http method
        this.method = HttpMethod.valueOf(extractFromLineGroup(str, "[]](.*?)[/]").replaceAll(" ", "")
                .replaceAll("\"", ""));

        // path
        this.path = extractFromLineGroup(str, "[ ][/](.*)[\\\"][ ][\\d]");

        // response code
        this.responseCode = Integer.parseInt(extractFromLineGroup(str, "[\\\"\\s](\\d{3})[\\s]").replaceAll(" ", ""));

        // response size
        this.responseSize = Integer.parseInt(extractFromLineGroup(str, "[\\s](\\d+\\s)[\\\"]").replaceAll(" ", ""));

        // referer
        this.referer = extractFromLineGroup(str, "[\\d][\\s][\\\"](.*)[\\\"][\\s][\\\"]");

        // agent
        UserAgent agent = new UserAgent(extractFromLineGroup(str, "[\\\"][\\s][\\\"](.*)[\\\"]"));
        this.agent = agent;

    }

    public static String extractFromLineGroup(String str, String regex) {

        if (str == null) return null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find() && !matcher.group(1).equals("-")) {
            return matcher.group(1);
        }
        return null;
    }

}
