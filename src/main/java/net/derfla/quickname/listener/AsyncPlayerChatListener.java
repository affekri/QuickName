package net.derfla.quickname.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.derfla.quickname.file.QuickNameFile;
import net.derfla.quickname.util.NameChange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncChatEvent event) {
        Component message = event.message();
        FileConfiguration file = QuickNameFile.get();
        for (String playerName : file.getConfigurationSection("players").getKeys(false)) {
            NamedTextColor color = NameChange.parseColor(file.getString("players." + playerName + ".color"));
            TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                    .match(playerName) // Target the placeholder
                    .replacement(Component.text(playerName).color(color)) // Replacement component
                    .build();
            message = message.replaceText(replacementConfig);
        }
        event.message(message);
    }
}
