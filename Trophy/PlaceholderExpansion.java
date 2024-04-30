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

public class XPThropy extends JavaPlugin implements Listener {
    private HashMap<UUID, Integer> trophies = new HashMap<>();

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        // Load player trophies from data storage (e.g., config file or database)
        // Example: loadPlayerTrophies();
        // Save default configuration if it doesn't exist
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Save player trophies to data storage (e.g., config file or database)
        // Example: savePlayerTrophies();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        // Check if the player has a bed
        // Example: if (hasBed(player)) {
        // Example:     removeTrophies(player, 1); // Remove 1 trophy if the bed was broken
        // Example: }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerTrophies(player);
    }

    // Example method to add trophies to a player
    public void addTrophies(Player player, int amount) {
        UUID playerId = player.getUniqueId();
        int currentTrophies = trophies.getOrDefault(playerId, 0);
        trophies.put(playerId, currentTrophies + amount);
        player.sendMessage(ChatColor.GREEN + "You received " + amount + " trophies!");
        // Example: updatePlayerScoreboard(player); // Update the player's scoreboard
    }

    // Example method to remove trophies from a player
    public void removeTrophies(Player player, int amount) {
        UUID playerId = player.getUniqueId();
        int currentTrophies = trophies.getOrDefault(playerId, 0);
        int newTrophies = Math.max(0, currentTrophies - amount);
        trophies.put(playerId, newTrophies);
        player.sendMessage(ChatColor.RED + "You lost " + amount + " trophies!");
        // Example: updatePlayerScoreboard(player); // Update the player's scoreboard
    }

    // Example method to save player trophies to data storage
    public void savePlayerTrophies(Player player) {
        UUID playerId = player.getUniqueId();
        int playerTrophies = trophies.getOrDefault(playerId, 0);
        // Save the player's trophies to data storage (e.g., config file or database)
        // Example: getConfig().set("players." + playerId.toString() + ".trophies", playerTrophies);
        // Example: saveConfig();
    }

    // Example method to load player trophies from data storage
    public void loadPlayerTrophies() {
        // Load player trophies from data storage (e.g., config file or database)
        // Example: ConfigurationSection playersSection = getConfig().getConfigurationSection("players");
        // Example: if (playersSection != null) {
        // Example:     for (String playerIdString : playersSection.getKeys(false)) {
        // Example:         UUID playerId = UUID.fromString(playerIdString);
        // Example:         int playerTrophies = playersSection.getInt(playerIdString + ".trophies");
        // Example:         trophies.put(playerId, playerTrophies);
        // Example:     }
        // Example: }
    }

    // Example method to check if a player has a bed
    public boolean hasBed(Player player) {
        // Implement logic to check if the player has a bed
        return false;
    }

    // Example method to update a player's scoreboard with their trophy count
    public void updatePlayerScoreboard(Player player) {
        // Get or create the player's scoreboard
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("trophies");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("trophies", "dummy", ChatColor.GOLD + "Trophies");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        // Update the player's trophy count on the scoreboard
        UUID playerId = player.getUniqueId();
        int playerTrophies = trophies.getOrDefault(playerId, 0);
        objective.setDisplayName(ChatColor.GOLD + "Trophies: " + playerTrophies);
    }

    // Method to show trophies for a player
    public void showTrophies(Player player) {
        int trophies = getPlayerTrophies(player);
        String trophiesPlaceholder = "%xpthropy_trophies%";
        String trophiesMessage = "Du hast " + trophiesPlaceholder + " Trophies!";
        player.sendMessage(trophiesMessage);
    }

    // Method to get player trophies
    public int getPlayerTrophies(Player player) {
        UUID playerId = player.getUniqueId();
        return trophies.getOrDefault(playerId, 0);
    }

    // Event handler for winning in Mong
    @EventHandler
    public void onMongWin(MongWinEvent event) {
        Player player = event.getPlayer();
        addTrophies(player, 5); // Add 5 trophies for winning in Mong
        // Display victory title and subtitle
        player.sendTitle(ChatColor.GREEN + "Victory", ChatColor.GRAY + "+ §35 §7Trophies", 10, 70, 20);
    }

    // Event handler for losing in Mong
    @EventHandler
    public void onMongLoss(MongLossEvent event) {
        Player player = event.getPlayer();
        removeTrophies(player, 3); // Remove 3 trophies for losing in Mong
        // Display defeat title and subtitle
        player.sendTitle(ChatColor.RED + "Defeat", ChatColor.GRAY + "- §43 §7Trophies", 10, 70, 20);
    }
}
