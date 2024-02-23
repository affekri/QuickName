package net.derfla.quickname.listener;

import net.derfla.quickname.util.NameChange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (NameChange.getColor(event.getPlayer()) == null) return;
        NamedTextColor color = NameChange.getColor(event.getPlayer());

        // Sets the list- and displayName
        NameChange.setCustomDisplayName(event.getPlayer());

        // Colorizes the joinMessage
        Component joinMessage = event.joinMessage();
        if (joinMessage == null) return;
        // Create TextReplacementConfig with replacement details
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .match(event.getPlayer().getName()) // Target the placeholder
                .replacement(Component.text(event.getPlayer().getName()).color(color)) // Replacement component
                .build();
        joinMessage = joinMessage.replaceText(replacementConfig);
        event.joinMessage(joinMessage);
    }
}
