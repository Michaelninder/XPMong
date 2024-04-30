import java.util.*;

public class TimeManager {
    private Map<String, Long> bestTimes = new HashMap<>();

    public void setBestTime(String playerName, long timeInSeconds) {
        bestTimes.put(playerName, timeInSeconds);
    }

    public long getBestTime(String playerName) {
        return bestTimes.getOrDefault(playerName, Long.MAX_VALUE);
    }

    public void updateBestTime(String playerName, long newTime) {
        long currentBestTime = getBestTime(playerName);
        if (newTime < currentBestTime) {
            setBestTime(playerName, newTime);
        }
    }
}
