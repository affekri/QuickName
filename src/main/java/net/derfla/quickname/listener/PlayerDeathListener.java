package net.derfla.quickname.listener;

import net.derfla.quickname.util.NameChange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (NameChange.getColor(event.getPlayer()) == null) return;
        NamedTextColor color = NameChange.getColor(event.getPlayer());
        Component deathMessage = event.deathMessage();
        if (deathMessage == null) return;
        // Create TextReplacementConfig with replacement details
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .match(event.getPlayer().getName()) // Target the placeholder
                .replacement(Component.text(event.getPlayer().getName()).color(color)) // Replacement component
                .build();
        deathMessage = deathMessage.replaceText(replacementConfig);
        event.deathMessage(deathMessage);
    }
}
