package net.derfla.quickname.listener;

import net.derfla.quickname.util.NameChange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class has one method, onPlayerQuit. Which is responsible for
 * replacing the original player name with the colorized one in the quit message.
 */
public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (NameChange.getColor(event.getPlayer()) == null) return;
        NamedTextColor color = NameChange.getColor(event.getPlayer());
        Component quitMessage = event.quitMessage();
        if (quitMessage == null) return;
        // Create TextReplacementConfig with replacement details
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .match(event.getPlayer().getName()) // Target the placeholder
                .replacement(Component.text(event.getPlayer().getName()).color(color)) // Replacement component
                .build();
        quitMessage = quitMessage.replaceText(replacementConfig);
        event.quitMessage(quitMessage);
    }
}
