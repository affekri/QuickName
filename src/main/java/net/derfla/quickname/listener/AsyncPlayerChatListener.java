package net.derfla.quickname.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.derfla.quickname.file.QuickNameFile;
import net.derfla.quickname.util.NameChange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.intellij.lang.annotations.RegExp;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has the method onAsyncPlayerChat which replaces "normal" versions of names with colored ones.
 */
public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncChatEvent event) {
        Component message = event.message();
        FileConfiguration file = QuickNameFile.get();
        ConfigurationSection players = file.getConfigurationSection("players");
        assert players != null;
        List<String> keys = new ArrayList<>(players.getKeys(false));
        int totalKeys = keys.size();

        for (String key : keys) {
            @RegExp String playerName = players.getString(key + ".name");
            NamedTextColor color = NameChange.parseColor(players.getString(key + ".color"));
            if (playerName == null) return;
            TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                    .match(playerName) // Target the placeholder
                    .replacement(Component.text(playerName).color(color)) // Replacement component
                    .build();
            message = message.replaceText(replacementConfig);
        }
        event.message(message);
    }
}
