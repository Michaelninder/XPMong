import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Teams extends JavaPlugin {
    private Map<String, String> teamNames = new HashMap<>();
    private Map<String, ChatColor> teamColors = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadTeams();
    }

    private void loadTeams() {
        FileConfiguration config = getConfig();
        if (config.contains("teams")) {
            for (String teamName : config.getConfigurationSection("teams").getKeys(false)) {
                String displayName = ChatColor.translateAlternateColorCodes('&', config.getString("teams." + teamName + ".name"));
                ChatColor color = ChatColor.valueOf(config.getString("teams." + teamName + ".color").toUpperCase());
                teamNames.put(teamName, displayName);
                teamColors.put(teamName, color);
            }
        }
    }

    public String getTeamDisplayName(String teamName) {
        return teamNames.getOrDefault(teamName.toLowerCase(), "");
    }

    public ChatColor getTeamColor(String teamName) {
        return teamColors.getOrDefault(teamName.toLowerCase(), ChatColor.WHITE);
    }
}
