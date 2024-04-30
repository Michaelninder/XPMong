import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CountdownAndInventory extends JavaPlugin implements Listener {
    private HashMap<Player, Integer> countdownTasks = new HashMap<>();

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        // Load configuration
        saveDefaultConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear(); // Clear player's inventory
        giveInventoryItems(player); // Give inventory items
        startCountdown(player); // Start countdown for the game to begin
    }

    private void startCountdown(Player player) {
        int countdownTime = getConfig().getInt("countdown_time", 15); // Get countdown time from config
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    player.sendMessage(ChatColor.YELLOW + "Game starting in " + timeLeft + " seconds...");
                    timeLeft--;
                } else {
                    player.sendMessage(ChatColor.GREEN + "Game starting!");
                    Bukkit.getScheduler().cancelTask(countdownTasks.get(player)); // Cancel the countdown task
                    // Start the game logic here
                }
            }
        }, 0L, 20L); // Run every second

        countdownTasks.put(player, taskId); // Store the task ID for the player
    }

    private void giveInventoryItems(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Inventory Sorter");

        // Fill the inventory sorter menu with items as needed
        // Example:
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Sort Inventory");
        item.setItemMeta(meta);

        menu.setItem(0, item);

        player.openInventory(menu);
    }
}
