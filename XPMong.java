import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.*;
import org.bukkit.scoreboard.*;

import java.util.*;

public class XPMong extends JavaPlugin implements Listener {
    private HashMap<Player, Integer> scores = new HashMap<>();
    private int gameTime = 0;
    private boolean gameRunning = false;
    private int countdownTime = 15; // Countdown time in seconds
    private Scoreboard scoreboard;
    private Objective objective;

    private ItemStack knockbackItem = new ItemStack(Material.STICK);
    private ItemStack spleeverItem = new ItemStack(Material.GOLD_PICKAXE);

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
        // Start game timer
        startGameTimer();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setknockback")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (!player.getWorld().getName().equalsIgnoreCase("Mong")) {
                player.sendMessage("You can only set knockback items in the Mong world!");
                return true;
            }
            if (args.length != 1) {
                player.sendMessage("Usage: /setknockback <material>");
                return true;
            }
            Material material = Material.matchMaterial(args[0]);
            if (material == null) {
                player.sendMessage("Invalid material!");
                return true;
            }
            knockbackItem = new ItemStack(material);
            player.sendMessage("Knockback item set to " + material.toString());
            return true;
        } else if (command.getName().equalsIgnoreCase("setspleever")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (!player.getWorld().getName().equalsIgnoreCase("Mong")) {
                player.sendMessage("You can only set spleever items in the Mong world!");
                return true;
            }
            if (args.length != 1) {
                player.sendMessage("Usage: /setspleever <material>");
                return true;
            }
            Material material = Material.matchMaterial(args[0]);
            if (material == null) {
                player.sendMessage("Invalid material!");
                return true;
            }
            spleeverItem = new ItemStack(material);
            player.sendMessage("Spleever item set to " + material.toString());
            return true;
        } else if (command.getName().equalsIgnoreCase("startgame")) {
            if (gameRunning) {
                sender.sendMessage("Game is already running!");
                return true;
            }
            if (Bukkit.getOnlinePlayers().size() < 2) {
                sender.sendMessage("Not enough players to start the game!");
                return true;
            }
            startCountdown();
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!gameRunning || !player.getWorld().getName().equalsIgnoreCase("Mong")) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().isSimilar(spleeverItem)) {
                // Implement spleever functionality
                // Determine block clicked and remove it if necessary
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!gameRunning || !player.getWorld().getName().equalsIgnoreCase("Mong")) {
            return;
        }
        if (event.getInventory().getHolder() == player) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                if (event.getSlot() == 0) {
                    knockbackItem = clickedItem;
                    player.sendMessage("Knockback item set to " + clickedItem.getType().toString());
                } else if (event.getSlot() == 1) {
                    spleeverItem = clickedItem;
                    player.sendMessage("Spleever item set to " + clickedItem.getType().toString());
                }
            }
        }
    }

    private void startGameTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                gameTime++;
                // Check for game end conditions
                if (gameTime >= 600 || checkWinConditions()) {
                    endGame();
                }
            }
        }.runTaskTimer(this, 20, 20); // Run every second
    }

    private void startCountdown() {
        gameRunning = true;
        new BukkitRunnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (timeLeft == 0) {
                    cancel();
                    // Start the game
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Game started!");
                    // Give players necessary items
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getInventory().clear();
                        player.getInventory().setItem(0, createTeamSelector());
                        player.getInventory().setItem(4, createInventorySorter());
                        player.getInventory().setItem(8, createItemSelector());
                    }
                } else {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Game starting in " + timeLeft + " seconds...");
                    timeLeft--;
                }
            }
        }.runTaskTimer(this, 0, 20); // Run every second
    }

    private ItemStack createTeamSelector() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Team Selector");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createInventorySorter() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Inventory Sorter");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItemSelector() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Item Selector");
        item.setItemMeta(meta);
        return item;
    }

    private boolean checkWinConditions() {
        // Implement win conditions here
        // Return true if a team has 10 points
        return false;
    }

    private void endGame() {
        gameRunning = false;
        // Implement game end logic here
        // Reset scores, refill platform, etc.
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (gameRunning && player.getWorld().getName().equalsIgnoreCase("Mong")) {
            setSpectator(player);
        }
    }

    private void setSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.getInventory().setItem(4, createSpectatorCompass());
        player.setInvisible(true);
        player.setAllowFlight(true);
    }

    private ItemStack createSpectatorCompass() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Spectator Menu");
        item.setItemMeta(meta);
        return item;
    }
}
