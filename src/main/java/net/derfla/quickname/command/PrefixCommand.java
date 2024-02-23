package net.derfla.quickname.command;

import net.derfla.quickname.QuickName;
import net.derfla.quickname.file.QuickNameFile;
import net.derfla.quickname.util.NameChange;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrefixCommand implements TabExecutor {

    static Plugin plugin = QuickName.getInstance();
    Style errorStyle = Style.style(NamedTextColor.RED, TextDecoration.BOLD);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Permission checking in plugin.yml
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("Could not find quickName.yml!");
            sender.sendMessage(Component.text("There seems to be a problem with the plugin setup. Please alert an admin!")
                    .color(NamedTextColor.YELLOW));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Component.text("Invalid arguments! Please use list, set, create or remove!").
                    style(errorStyle));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "list":
                // Send a list of all prefixes
                String prefixes;
                List<String> fromFile = NameChange.getAllPrefixes();
                prefixes = fromFile.toString();
                sender.sendMessage(Component.text("Here are all prefixes on this server: " + prefixes));
                return true;
            case "set":
                // Setting the prefix of a player, also handles "unsetting" prefix
                Player subject = Bukkit.getPlayerExact(args[1]);
                if (subject == null) {
                    sender.sendMessage(Component.text("Please provide an online player!")
                            .style(errorStyle));
                    return true;
                }
                if (args.length == 2) {
                    file.set("players." + subject.getName() + ".prefix", null);
                    QuickNameFile.save();
                    sender.sendMessage(Component.text("Unset the prefix of " + subject.getName())
                            .color(NamedTextColor.YELLOW));
                    NameChange.setCustomDisplayName(subject);
                    return true;
                }
                if (!file.contains("prefix." + args[2])) {
                    sender.sendMessage(Component.text("The prefix " + args[2] + " does not exist!")
                            .style(errorStyle));
                    return true;
                }
                file.set("players." + subject.getName() + ".prefix", args[2]);
                QuickNameFile.save();
                NameChange.setCustomDisplayName(subject);
                sender.sendMessage(Component.text("Set the prefix of " + subject.getName() + " to: ")
                        .color(NamedTextColor.YELLOW)
                        .append(NameChange.getPrefix(subject)));
                return true;

            case "create":
                // Create a new prefix
                if (args.length != 5) {
                    sender.sendMessage(Component.text("Invalid amount of arguments!")
                            .style(errorStyle));
                    return true;
                }
                if (NameChange.parseColor(args[3]) == null) {
                    sender.sendMessage(Component.text("Please provide a color!")
                            .style(errorStyle));
                    return true;
                }

                NameChange.createPrefix(args[1], args[2], NameChange.parseColor(args[3]), Boolean.parseBoolean(args[4]));
                sender.sendMessage(Component.text("Created the prefix: " + args[1])
                        .color(NamedTextColor.YELLOW));
                return true;
            case "remove":
                // Remove a prefix

                if (!file.contains("prefix." + args[0])) {
                    sender.sendMessage(Component.text("The prefix that you provided does not exist!")
                            .style(errorStyle));
                    return true;
                }
            default:
                sender.sendMessage(Component.text("Invalid argument! Please use list, set, create or remove!").
                        style(errorStyle));
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("set", "create", "remove", "list")
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        switch (args[0].toLowerCase()) {
            case "set":
                switch (args.length) {
                    case 2:
                        return Bukkit.getOnlinePlayers().stream()
                                // Filter players whose names start with the input
                                .map(Player::getName)
                                .filter(name -> name.startsWith(args[1]))
                                .collect(Collectors.toList());
                    case 3:
                        return NameChange.getAllPrefixes().stream()
                                .filter(prefix -> prefix.startsWith(args[2].toLowerCase()))
                                .collect(Collectors.toList());
                    default:
                        return Collections.emptyList();
                }
            case "create":
                switch (args.length) {
                    case 2:
                        return Collections.singletonList("prefixID");
                    case 3:
                        return Collections.singletonList("prefixText");
                    case 4:
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
                        return colorNames.stream()
                                .filter(color -> color.toLowerCase().startsWith(args[3].toLowerCase()))
                                .collect(Collectors.toList());
                    case 5:
                        return Stream.of("true", "false")
                                .filter(bool -> bool.startsWith(args[4].toLowerCase()))
                                .collect(Collectors.toList());
                    default:
                        return Collections.emptyList();
                }
            case "remove":
                if (args.length == 2) {
                    return NameChange.getAllPrefixes().stream()
                            .filter(prefix -> prefix.startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                } else return Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }
}
