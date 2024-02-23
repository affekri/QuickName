package net.derfla.quickname.file;

import net.derfla.quickname.QuickName;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class QuickNameFile {
    private static File file;
    private static FileConfiguration customFile;
    static Plugin plugin = QuickName.getInstance();

    public static void setup(){
        // Setup logic, gets called when plugin is loaded
        file = new File(plugin.getDataFolder(), "quickName.yml");

        if(!file.exists()){
            try {
                file.createNewFile();
                plugin.getLogger().info("Created new file: " + file.getName());
            } catch (IOException e) {
                // Just sends out a warning
                e.printStackTrace();
                plugin.getLogger().warning("Couldn't create file: " + file.getName());
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        // Gets the file
        return customFile;
    }

    public static void save(){
        // Saves the file
        try {
            customFile.save(file);
        } catch (IOException e) {
            // Just sends out a warning
            plugin.getLogger().warning("Couldn't save file: " + file.getName());
        }
    }

    public static void reload(){
        // Reloads the file
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}
