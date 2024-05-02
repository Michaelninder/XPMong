import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Setup extends JavaPlugin {
    private Location spawnPoint;
    private Location pos1;
    private Location pos2;
    private Location redSpawn;
    private Location blueSpawn;
    private Location redBed;
    private Location blueBed;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadLocations();
    }

    private void loadLocations() {
        FileConfiguration config = getConfig();
        spawnPoint = getLocation(config, "spawnPoint");
        pos1 = getLocation(config, "pos1");
        pos2 = getLocation(config, "pos2");
        redSpawn = getLocation(config, "Red_spawn");
        blueSpawn = getLocation(config, "Blue_spawn");
        redBed = getLocation(config, "Red_bed");
        blueBed = getLocation(config, "Blue_bed");
    }

    private Location getLocation(FileConfiguration config, String path) {
        if (config.contains(path)) {
            ConfigurationSection section = config.getConfigurationSection(path);
            if (section != null) {
                double x = section.getDouble("x");
                double y = section.getDouble("y");
                double z = section.getDouble("z");
                return new Location(getServer().getWorlds().get(0), x, y, z);
            }
        }
        return null;
    }

    public void setSpawnPoint(Player player) {
        setLocation(player.getLocation(), "spawnPoint");
    }

    public void setPos1(Player player) {
        setLocation(player.getLocation(), "pos1");
    }

    public void setPos2(Player player) {
        setLocation(player.getLocation(), "pos2");
    }

    public void setRedSpawn(Player player) {
        setLocation(player.getLocation(), "Red_spawn");
    }

    public void setBlueSpawn(Player player) {
        setLocation(player.getLocation(), "Blue_spawn");
    }

    public void setRedBed(Player player) {
        setLocation(player.getLocation(), "Red_bed");
    }

    public void setBlueBed(Player player) {
        setLocation(player.getLocation(), "Blue_bed");
    }

    private void setLocation(Location location, String path) {
        FileConfiguration config = getConfig();
        ConfigurationSection section = config.createSection(path);
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        saveConfig();
        loadLocations(); // Reload locations after saving
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public Location getRedBed() {
        return redBed;
    }

    public Location getBlueBed() {
        return blueBed;
    }

    public boolean saveMap(String mapName) {
        File mapFile = new File(getDataFolder(), mapName + ".yml");
        if (!mapFile.exists()) {
            try {
                mapFile.createNewFile();
                FileConfiguration mapConfig = new YamlConfiguration();
                mapConfig.set("spawnPoint", getLocationString(spawnPoint));
                mapConfig.set("pos1", getLocationString(pos1));
                mapConfig.set("pos2", getLocationString(pos2));
                mapConfig.set("Red_spawn", getLocationString(redSpawn));
                mapConfig.set("Blue_spawn", getLocationString(blueSpawn));
                mapConfig.set("Red_bed", getLocationString(redBed));
                mapConfig.set("Blue_bed", getLocationString(blueBed));
                mapConfig.save(mapFile);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private String getLocationString(Location location) {
        if (location != null) {
            return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
        }
        return "";
    }
}
