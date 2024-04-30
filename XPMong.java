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
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!player.getWorld().getName().equalsIgnoreCase("Mong")) {
                return;
            }
            if (player.getInventory().getItemInMainHand().isSimilar(spleeverItem)) {
                // Implement spleever functionality
                // Determine block clicked and remove it if necessary
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() == player && player.getWorld().getName().equalsIgnoreCase("Mong")) {
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

    private boolean checkWinConditions() {
        // Implement win conditions here
        // Return true if a team has 10 points
        return false;
    }

    private void endGame() {
        // Implement game end logic here
        // Reset scores, refill platform, etc.
    }
}
