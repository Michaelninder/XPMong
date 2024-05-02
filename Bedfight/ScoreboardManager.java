import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    private Map<Player, Scoreboard> scoreboards = new HashMap<>();
    private final XPBedfight plugin;

    public ScoreboardManager(XPBedfight plugin) {
        this.plugin = plugin;
    }

    public void updateScoreboard(Player player) {
        FileConfiguration scoreboardConfig = plugin.getScoreboardConfig();

        boolean redHasBed = scoreboardConfig.getBoolean("RedTeam.HasBed");
        boolean blueHasBed = scoreboardConfig.getBoolean("BlueTeam.HasBed");

        setBedIcons(player, redHasBed, blueHasBed);
        showScoreboard(player, redHasBed, blueHasBed);
    }

    private void setBedIcons(Player player, boolean redHasBed, boolean blueHasBed) {
        Scoreboard scoreboard = scoreboards.get(player);
        if (scoreboard != null) {
            Team redTeam = scoreboard.getTeam("redTeam");
            if (redTeam != null) {
                redTeam.setSuffix(redHasBed ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘");
            }

            Team blueTeam = scoreboard.getTeam("blueTeam");
            if (blueTeam != null) {
                blueTeam.setSuffix(blueHasBed ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘");
            }
        }
    }

    private void showScoreboard(Player player, boolean redHasBed, boolean blueHasBed) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("bedfight", "dummy", ChatColor.translateAlternateColorCodes('&', "&6&lBEDFIGHT"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team redTeam = scoreboard.registerNewTeam("redTeam");
        redTeam.addEntry(ChatColor.RED + "Red Team: ");
        redTeam.setPrefix(ChatColor.RED.toString());
        redTeam.setSuffix(redHasBed ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘");

        Team blueTeam = scoreboard.registerNewTeam("blueTeam");
        blueTeam.addEntry(ChatColor.BLUE + "Blue Team: ");
        blueTeam.setPrefix(ChatColor.BLUE.toString());
        blueTeam.setSuffix(blueHasBed ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘");

        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.RED + "Red Team: ").setScore(2);
        obj.getScore(ChatColor.BLUE + "Blue Team: ").setScore(1);

        player.setScoreboard(scoreboard);
        scoreboards.put(player, scoreboard);
    }

    public void hideScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        scoreboards.remove(player);
    }
}
