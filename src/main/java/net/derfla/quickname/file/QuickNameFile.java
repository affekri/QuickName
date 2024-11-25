package net.derfla.quickname.file;

import net.derfla.quickname.QuickName;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * This class handles all logic regarding setting up, writing and reading of the QuickName.yml file.
 */
public class QuickNameFile {
    private static File file;
    private static FileConfiguration customFile;
    static Plugin plugin = QuickName.getInstance();

    /**
     * Sets up the file QuickName.yml.
     * If the file doesn't exist it creates a new one and loads it.
     * If the file exists already this method just load the file.
     */
    public static void setup() {
        // Setup logic, gets called when plugin is loaded
        file = new File(plugin.getDataFolder(), "QuickName.yml");

        if (!file.exists()) {
            try {
                if (file.createNewFile()) plugin.getLogger().info("Created new file: " + file.getName());
            } catch (IOException e) {
                // Just sends out a warning
                plugin.getLogger().warning("Couldn't create file: " + file.getName() + " ; " + e.getMessage());
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * This method is used to read from the file QuickName.yml.
     * @return Returns file as a FileConfiguration
     */
    public static FileConfiguration get() {
        // Gets the file
        return customFile;
    }

    /**
     * Writes the changed lines to the file.
     * This should be called whenever trying to write to the file!
     */
    public static void save() {
        // Saves the file
        try {
            customFile.save(file);
        } catch (IOException e) {
            // Just sends out a warning
            plugin.getLogger().warning("Couldn't save file: " + file.getName());
        }
    }

    public static void reload() {
        // Reloads the file
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
