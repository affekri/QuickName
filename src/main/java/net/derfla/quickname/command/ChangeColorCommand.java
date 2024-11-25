package net.derfla.quickname.command;

import net.derfla.quickname.util.NameChange;
import net.derfla.quickname.util.Styles;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class has two methods, onCommand which is responsible for colorizing player names,
 * and onTabComplete which gives the player suggestions on what arguments to use when utilizing the command.
 * Most of the actual color logic is passed on to the class NameChange.
 */
public class ChangeColorCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Permission checking is done by plugin.yml
        if (args.length != 2) {
            sender.sendMessage(Component.text("Invalid amount of arguments!")
                    .style(Styles.ERRORSTYLE));
            return true;
        }
        Player subject = Bukkit.getServer().getPlayerExact(args[0]);
        if (subject == null) {
            sender.sendMessage(Component.text("Please provide an online player!")
                    .style(Styles.ERRORSTYLE));
            return true;
        }
        if (NameChange.parseColor(args[1]) == null) {
            sender.sendMessage(Component.text("Please provide a color!")
                    .style(Styles.ERRORSTYLE));
            return true;
        }
        NamedTextColor color = NameChange.parseColor(args[1]);

        NameChange.saveToFile(subject, color);
        NameChange.setCustomDisplayName(subject);

        sender.sendMessage(Component.text("Changed color of " + subject.getName() + " to ")
                .style(Styles.INFOSTYLE)
                .append(Component.text(subject.getName()).color(color)));

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1){
            String playerInput = args[0]; // Get the player's current input
            return Bukkit.getOnlinePlayers().stream()
                    // Filter players whose names start with the input
                    .filter(player -> player.getName().startsWith(playerInput))
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            List<String> colorNames = new ArrayList<>();
            try {
                Class<?> colorClass = NamedTextColor.class;
                Field[] fields = colorClass.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType().equals(NamedTextColor.class)) {
                        field.setAccessible(true);
                        colorNames.add(field.getName());
                    }
                }
            } catch (Exception e) {
                // Handle exceptions gracefully (e.g., logging, returning empty list)
            }
            String colorInput = args[1].toLowerCase();
            return colorNames.stream()
                    .filter(color -> color.toLowerCase().startsWith(colorInput))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }

    }
}
