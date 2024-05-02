import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Scoreboard implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("bedfight.scoreboard")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        // Check if player is in game
        if (!GameManager.isInGame(player)) {
            player.sendMessage("You are not in a Bedfight game.");
            return true;
        }

        // Get or create the scoreboard
        ScoreboardManager manager = player.getServer().getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("bedfight", "dummy", "BEDFIGHT");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set title format
        objective.setDisplayName("§e§lBEDFIGHT");

        // Get team data (you need to implement this)
        TeamData redTeamData = TeamManager.getTeamData("red");
        TeamData blueTeamData = TeamManager.getTeamData("blue");

        // Check if teams have beds
        String redBedIcon = redTeamData.hasBed() ? "✔" : "✘";
        String blueBedIcon = blueTeamData.hasBed() ? "✔" : "✘";

        // Set scoreboard lines
        objective.getScore("").setScore(6);
        objective.getScore("Red Team: " + redBedIcon).setScore(5);
        objective.getScore("Blue Team: " + blueBedIcon).setScore(4);
        objective.getScore("").setScore(3);
        objective.getScore("§eXP-CRaft.mcplay.fun").setScore(2);

        // Assign scoreboard to player
        player.setScoreboard(scoreboard);

        return true;
    }
}
