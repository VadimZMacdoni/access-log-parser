import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    private final String operationalSystem;
    private final String browser;
    private boolean isBot = false;

    public String getOperationalSystem() {
        return operationalSystem;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean getIsBot() {
        return isBot;
    }

    public UserAgent(String str) {

        // browser
        if (str == null) {
            this.browser = null;
        } else if (str.contains("Edg")) {
            this.browser = "Edge";
        } else if (str.contains("OPR") || str.contains("Opera")) {
            this.browser = "Opera";
        } else if (str.contains("Safari")) {
            this.browser = "Safari";
        } else if (str.contains("Chrome")) {
            this.browser = "Chrome";
        } else if (str.contains("Mozilla")) {
            this.browser = "Firefox";
        } else {
            this.browser = null;
        }

        // OS
        String partStr = extractFromLineGroup(str, "[\\s][(](.{20}).");

        if (partStr == null) {
            this.operationalSystem = null;
        } else if (partStr.contains("Linux")) {
            this.operationalSystem = "Linux";
        } else if (partStr.contains("Windows")) {
            this.operationalSystem = "Windows";
        } else if (partStr.contains("Macintosh")) {
            this.operationalSystem = "Mac OS";
        } else if (partStr.contains("compatible")) {
            this.operationalSystem = "compatible";
        } else {
            this.operationalSystem = null;
        }
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

    public void isBot(String str) {

        if (str.contains("Bot") || str.contains("bot")) this.isBot = true;

    }
}
