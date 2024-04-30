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

public class CustomSpleeverItem extends JavaPlugin implements Listener {

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
            openCustomSpleeverMenu(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Custom Spleever Item")) {
            event.setCancelled(true); // Cancel the event to prevent moving items in the menu
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            if (clickedItem.getItemMeta() != null) {
                // Handle click event for custom spleever items
            }
        }
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
