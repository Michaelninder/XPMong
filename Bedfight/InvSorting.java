import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class InvSorting extends JavaPlugin implements Listener {
    private FileConfiguration sortingConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        sortingConfig = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.equals(player.getOpenInventory().getTopInventory())) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                int slot = event.getRawSlot();
                if (sortingConfig.contains("items." + slot)) {
                    // Execute sorting action based on slot
                    String action = sortingConfig.getString("items." + slot + ".action");
                    if (action != null && !action.isEmpty()) {
                        // Perform sorting action
                        performSortingAction(player, action);
                    }
                }
            }
        }
    }

    private void performSortingAction(Player player, String action) {
        // Perform sorting action based on the action type
        switch (action.toLowerCase()) {
            case "sort_inventory":
                sortInventory(player);
                break;
            default:
                // Do nothing for unrecognized actions
                break;
        }
    }

    private void sortInventory(Player player) {
        // Sort the player's inventory
        player.sendMessage(ChatColor.GREEN + "Sorting inventory...");
        player.getInventory().sort(null);
        player.sendMessage(ChatColor.GREEN + "Inventory sorted!");
    }

    public void openSortingMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', sortingConfig.getString("title", "Inventory Sorting Menu")));

        // Load items from config
        Map<String, Object> items = sortingConfig.getConfigurationSection("items").getValues(false);
        for (Map.Entry<String, Object> entry : items.entrySet()) {
            int slot = Integer.parseInt(entry.getKey());
            ItemStack item = (ItemStack) entry.getValue();
            inventory.setItem(slot, item);
        }

        player.openInventory(inventory);
    }
}
