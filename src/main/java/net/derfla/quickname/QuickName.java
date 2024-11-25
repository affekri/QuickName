package net.derfla.quickname;

import net.derfla.quickname.command.ChangeColorCommand;
import net.derfla.quickname.command.PrefixCommand;
import net.derfla.quickname.file.QuickNameFile;
import net.derfla.quickname.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuickName extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("changecolor").setExecutor(new ChangeColorCommand());
        getCommand("prefix").setExecutor(new PrefixCommand());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        QuickNameFile.setup();
        QuickNameFile.get().options().copyDefaults(true);
        QuickNameFile.save();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static QuickName getInstance() {
        return getPlugin(QuickName.class);
    }
}
