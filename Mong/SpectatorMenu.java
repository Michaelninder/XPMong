import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SpectatorMenu extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Check if the player has permission to join Mong game
        if (player.hasPermission("Mong.game.join")) {
            // Teleport the player to the spectator spawn point
            // startCountdown(player); // Start countdown for the game to begin
            openSpectatorMenu(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Spectator Menu")) {
            event.setCancelled(true); // Cancel the event to prevent moving items in the menu
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            if (clickedItem.getItemMeta() != null) {
                if (clickedItem.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Return to Lobby")) {
                    player.performCommand("lobby"); // Change "lobby" to the command to return to the lobby
                    player.closeInventory();
                }
                // Add more actions for other items in the menu
            }
        }
    }

    private void openSpectatorMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Spectator Menu");

        // Fill the spectator menu with items as needed
        // Example:
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Return to Lobby");
        item.setItemMeta(meta);

        menu.setItem(0, item);

        player.openInventory(menu);
    }
}
