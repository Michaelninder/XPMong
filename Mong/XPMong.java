import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class XPMong extends JavaPlugin implements Listener {
    private HashMap<Player, Integer> countdownTasks = new HashMap<>();

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        // Load configuration
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mong")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ChatColor.YELLOW + "Mong commands:");
                // Add your commands here
                return true;
            }
            // Handle subcommands here
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Check if the player has permission to join Mong game
        if (player.hasPermission("Mong.game.join")) {
            player.getInventory().clear(); // Clear player's inventory
            giveInventoryItems(player); // Give inventory items
            startCountdown(player); // Start countdown for the game to begin
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.getType() == Material.DIAMOND_PICKAXE) {
                openCustomSpleeverMenu(player);
            }
            // Add more item interactions here
        }
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

    private void openCustomSpleeverMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Custom Spleever Item");

        // Fill the custom spleever item menu with items as needed
        // Example:
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Diamond Pickaxe");
        item.setItemMeta(meta);

        menu.setItem(0, item);

        player.openInventory(menu);
    }
}
