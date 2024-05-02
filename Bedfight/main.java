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

public class XPBedfight extends JavaPlugin implements Listener {
    private Map<UUID, Boolean> playersReady = new HashMap<>();
    private Map<UUID, Boolean> playersAlive = new HashMap<>();
    private Map<UUID, Location> playerSpawnPoints = new HashMap<>();
    private Map<UUID, BukkitTask> respawnTasks = new HashMap<>();
    private int minRespawnHeight = 10; // Minimum Y-coordinate for respawn
    private int respawnDelay = 5; // Respawn delay in seconds (default: 5 seconds)

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        // Load configuration
        saveDefaultConfig();
        reloadConfig();
        minRespawnHeight = getConfig().getInt("min_respawn_height", 10);
        respawnDelay = getConfig().getInt("respawn_delay", 5);
    }

    @Override
    public void onDisable() {
        // Cleanup tasks or save data if necessary
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("bedfight")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("join")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    joinBedfight(player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can join Bedfight!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /bedfight join");
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();
        // Respawn the player after a delay
        respawnTasks.put(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                respawnPlayer(player);
            }
        }.runTaskLater(this, respawnDelay * 20));
        // Check if the player's bed is destroyed
        if (player.getBedSpawnLocation() == null) {
            // Handle bed destroyed condition
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // Cancel any existing respawn tasks
        if (respawnTasks.containsKey(uuid)) {
            respawnTasks.get(uuid).cancel();
            respawnTasks.remove(uuid);
        }
        // Set respawn location
        Location respawnLocation = playerSpawnPoints.getOrDefault(uuid, getServer().getWorlds().get(0).getSpawnLocation());
        event.setRespawnLocation(respawnLocation);
        // Give initial items
        giveInitialItems(player);
        // Mark player as alive
        playersAlive.put(uuid, true);
    }

    private void joinBedfight(Player player) {
        UUID uuid = player.getUniqueId();
        // Clear player inventory
        player.getInventory().clear();
        // Add cancel item to slot 9
        player.getInventory().setItem(8, new ItemStack(Material.RED_DYE));
        // Mark player as ready
        playersReady.put(uuid, true);
        // Check if there's another ready player
        for (boolean ready : playersReady.values()) {
            if (ready) {
                startGame();
                return;
            }
        }
    }

    private void startGame() {
        // Logic to start the game and teleport players to their spawn points
        for (UUID uuid : playersReady.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                // Teleport player to spawn point
                Location spawnPoint = playerSpawnPoints.getOrDefault(uuid, player.getWorld().getSpawnLocation());
                player.teleport(spawnPoint);
                // Mark player as alive
                playersAlive.put(uuid, true);
            }
        }
    }

    private void respawnPlayer(Player player) {
        if (player.getLocation().getBlockY() < minRespawnHeight) {
            // Teleport player to spawn point
            UUID uuid = player.getUniqueId();
            Location spawnPoint = playerSpawnPoints.getOrDefault(uuid, player.getWorld().getSpawnLocation());
            player.teleport(spawnPoint);
            // Give initial items
            giveInitialItems(player);
            // Mark player as alive
            playersAlive.put(uuid, true);
        }
    }

    private void giveInitialItems(Player player) {
        // Give initial items to the player
        ItemStack woolStack = new ItemStack(Material.WOOL, 64, (short) getTeamColor(player).getWoolData());
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack axe = new ItemStack(Material.WOODEN_AXE);
        ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
        player.getInventory().addItem(woolStack, sword, axe, pickaxe);
    }

    private DyeColor getTeamColor(Player player) {
        // Determine player's team color (default to red)
        return player.getName().toLowerCase().contains("rou") ? DyeColor.RED : DyeColor.BLUE;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Prevent breaking blocks except beds, wool, end stone, and wood
        Material blockType = event.getBlock().getType();
        if (blockType != Material.BED_BLOCK &&
                blockType != Material.WOOL &&
                blockType != Material.END_STONE &&
                blockType != Material.WOOD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Prevent placing blocks except beds, wool, end stone, and wood
        Material blockType = event.getBlock().getType();
        if (blockType != Material.BED_BLOCK &&
                blockType != Material.WOOL &&
                blockType != Material.END_STONE &&
                blockType != Material.WOOD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // Remove player from game data
        playersReady.remove(uuid);
        playersAlive.remove(uuid);
        playerSpawnPoints.remove(uuid);
        // Cancel any respawn tasks
        if (respawnTasks.containsKey(uuid)) {
            respawnTasks.get(uuid).cancel();
            respawnTasks.remove(uuid);
        }
    }

    // Add inventory sorting menu logic here
}
