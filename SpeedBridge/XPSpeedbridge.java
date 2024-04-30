import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.*;
import org.bukkit.scoreboard.*;

import java.util.*;

public class XPSpeedbridge extends JavaPlugin implements Listener {
    private Map<UUID, Long> startTime = new HashMap<>();
    private Map<UUID, Long> bestTime = new HashMap<>();
    private Map<UUID, BukkitTask> bossBarTasks = new HashMap<>();
    private Map<UUID, Integer> trophies = new HashMap<>();
    private int trophyReward = 1; // Number of trophies to reward per second
    private TimeManager timeManager;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        timeManager = new TimeManager();
    }

    @Override
    public void onDisable() {
        for (BukkitTask task : bossBarTasks.values()) {
            task.cancel();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (label.equalsIgnoreCase("sw") && args.length > 0 && args[0].equalsIgnoreCase("settings")) {
            openSettingsMenu(player);
        }
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!startTime.containsKey(uuid) || !bestTime.containsKey(uuid)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime.get(uuid);
        String progress = getProgressBar(player.getLocation().getBlockX(), bestTime.get(uuid));
        player.sendActionBar(ChatColor.GREEN + progress + " Time: " + elapsedTime / 1000 + "s");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!startTime.containsKey(uuid)) {
            startTime.put(uuid, System.currentTimeMillis());
        }
    }

    private String getProgressBar(int current, long best) {
        int maxLength = 20;
        int progress = (int) ((double) current / best * maxLength);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            if (i < progress) {
                bar.append(ChatColor.GREEN).append("|");
            } else {
                bar.append(ChatColor.RED).append("|");
            }
        }
        return bar.toString();
    }

    private void startBridge(Player player) {
        Location startLocation = player.getLocation().clone();
        World world = startLocation.getWorld();
        if (world == null) return;

        UUID uuid = player.getUniqueId();
        if (!startTime.containsKey(uuid)) {
            startTime.put(uuid, System.currentTimeMillis());
        }

        bossBarTasks.put(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime.get(uuid);
                double progress = (double) elapsedTime / 1000;
                player.sendActionBar(ChatColor.GREEN + "Progress: " + progress + "s");
            }
        }.runTaskTimer(this, 0, 20));

        // Code to build bridge using sandstone
        Location endLocation = startLocation.clone().add(10, 0, 0);
        for (int i = 0; i < 10; i++) {
            Location bridgeLocation = startLocation.clone().add(i, 0, 0);
            bridgeLocation.getBlock().setType(Material.SANDSTONE);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(endLocation);
                player.sendTitle(ChatColor.GREEN + "Congratulations!", "You completed the speedbridge!", 10, 70, 20);
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime.get(uuid);
                long currentBestTime = bestTime.getOrDefault(uuid, Long.MAX_VALUE);
                if (elapsedTime < currentBestTime) {
                    bestTime.put(uuid, elapsedTime);
                    player.sendMessage(ChatColor.GREEN + "New personal best time: " + elapsedTime / 1000 + "s");
                    // Reward trophies based on bridge completion time
                    int trophiesEarned = (int) (elapsedTime / 1000) * trophyReward;
                    addTrophies(player, trophiesEarned);
                    // Update best time
                    timeManager.updateBestTime(player.getName(), elapsedTime);
                } else {
                    player.sendMessage(ChatColor.GREEN + "Your time: " + elapsedTime / 1000 + "s");
                }
                startTime.remove(uuid);
                bossBarTasks.get(uuid).cancel();
                bossBarTasks.remove(uuid);
            }
        }.runTaskLater(this, 200);
    }

    private void openSettingsMenu(Player player) {
        Inventory settingsMenu = Bukkit.createInventory(player, 9, ChatColor.GREEN + "Settings");

        // Add map selection option
        ItemStack mapItem = new ItemStack(Material.MAP);
        ItemMeta mapMeta = mapItem.getItemMeta();
        mapMeta.setDisplayName(ChatColor.YELLOW + "Map Selection");
        mapItem.setItemMeta(mapMeta);
        settingsMenu.setItem(0, mapItem);

        // Add spawn offset option
        ItemStack offsetItem = new ItemStack(Material.COMPARATOR);
        ItemMeta offsetMeta = offsetItem.getItemMeta();
        offsetMeta.setDisplayName(ChatColor.YELLOW + "Spawn Offset");
        offsetItem.setItemMeta(offsetMeta);
        settingsMenu.setItem(1, offsetItem);

        // Add block selection option
        ItemStack blockItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta blockMeta = blockItem.getItemMeta();
        blockMeta.setDisplayName(ChatColor.YELLOW + "Block Selection");
        blockItem.setItemMeta(blockMeta);
        settingsMenu.setItem(2, blockItem);

        // Add timer display option
        ItemStack timerItem = new ItemStack(Material.CLOCK);
        ItemMeta timerMeta = timerItem.getItemMeta();
        timerMeta.setDisplayName(ChatColor.YELLOW + "Timer Display");
        timerItem.setItemMeta(timerMeta);
        settingsMenu.setItem(3, timerItem);

        player.openInventory(settingsMenu);
    }

    private void addTrophies(Player player, int amount) {
        UUID playerId = player.getUniqueId();
        int currentTrophies = trophies.getOrDefault(playerId, 0);
        trophies.put(playerId, currentTrophies + amount);
        player.sendMessage(ChatColor.GREEN + "You received " + amount + " trophies!");
    }
}
