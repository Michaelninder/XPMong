import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.*;
import org.bukkit.scoreboard.*;

import java.util.*;

public class XPMong extends JavaPlugin implements Listener {
    private HashMap<String, Location> spawnPoints = new HashMap<>();

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        // Load spawn points from data storage
        // Example: loadSpawnPoints();
        // Register commands
        registerCommands();
        // Save default configuration if it doesn't exist
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Save spawn points to data storage
        // Example: saveSpawnPoints();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Check if the player has permission to join Mong game
        if (player.hasPermission("Mong.game.join")) {
            // Teleport the player to the spectator spawn point
            player.teleport(getSpawnPoint("spectator"));
        }
    }

    // Method to set a spawn point
    public void setSpawnPoint(String spawnType, Location location) {
        spawnPoints.put(spawnType.toLowerCase(), location);
        // Example: saveSpawnPoints();
    }

    // Method to get a spawn point
    public Location getSpawnPoint(String spawnType) {
        return spawnPoints.getOrDefault(spawnType.toLowerCase(), null);
    }

    // Command to set a spawn point
    public void setSpawnCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /mong setspawn <spawnType>");
            return;
        }

        String spawnType = args[1].toLowerCase();
        Location playerLocation = player.getLocation();
        setSpawnPoint(spawnType, playerLocation);

        player.sendMessage(ChatColor.GREEN + "Spawn point for " + spawnType + " set!");
    }

    // Command executor for setspawn command
    private class SetSpawnCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            // Check if the player has permission to set spawn
            if (player.hasPermission("Mong.admin.setspawn")) {
                setSpawnCommand(player, args);
            } else {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            }
            return true;
        }
    }

    // Register commands and listeners
    private void registerCommands() {
        getCommand("mong").setExecutor(new SetSpawnCommandExecutor());
    }
}
