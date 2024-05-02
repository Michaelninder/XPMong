import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setup implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("bedfight.setup")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /setup <death_height>");
            return true;
        }

        int deathHeight;
        try {
            deathHeight = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid death height. Please enter a number.");
            return true;
        }

        // Set death height in config or wherever you store it
        // Config.setDeathHeight(deathHeight);

        // Set spawn points, etc.

        player.sendMessage("Bedfight setup complete!");
        return true;
    }
}
