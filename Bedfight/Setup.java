import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            return new Location(getServer().getWorlds().get(0), x, y, z);
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
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        saveConfig();
        switch (path) {
            case "spawnPoint":
                spawnPoint = location;
                break;
            case "pos1":
                pos1 = location;
                break;
            case "pos2":
                pos2 = location;
                break;
            case "Red_spawn":
                redSpawn = location;
                break;
            case "Blue_spawn":
                blueSpawn = location;
                break;
            case "Red_bed":
                redBed = location;
                break;
            case "Blue_bed":
                blueBed = location;
                break;
        }
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
}
