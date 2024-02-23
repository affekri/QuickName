package net.derfla.quickname.util;

import net.derfla.quickname.QuickName;
import net.derfla.quickname.file.QuickNameFile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameChange {

    static Plugin plugin = QuickName.getInstance();
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void setCustomDisplayName(Player player) {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("quickName.yml was not found!");
            return;
        }
        Component prefix = getPrefix(player);


        NamedTextColor playerNameColor = getColor(player);
        Component originalName = player.name();
        Style style = Style.style(playerNameColor);
        Component coloredName = originalName.style(style);

        player.displayName(coloredName);
        player.playerListName(prefix.append(coloredName));
    }

    public static NamedTextColor getColor(Player player) {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("quickName.yml was not found!");
            return null;
        }
        if (!file.contains("players." + player.getName() + ".color")) return null;
        if (file.get("players." + player.getName() + ".color") == null) return null;
        NamedTextColor color = parseColor(file.getString("players." + player.getName() + ".color"));

        return color;
    }

    public static NamedTextColor parseColor(String string) {
        // Parsing the string input to a NamedTextColor
        if (string == null || string.isEmpty()) {
            return null;
        }
        // Attempt to parse using MiniMessage's color section syntax
        try {
            Component coloredText = miniMessage.deserialize("<" + string + ">");
            if (coloredText.color() instanceof NamedTextColor) {
                return (NamedTextColor) coloredText.color();
            } else {
                return null;  // Or return a default color if you prefer
            }
        } catch (Exception e) {
            // Handle invalid color format
            return null;
        }
    }

    public static void saveToFile(Player player, NamedTextColor color) {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("quickName.yml was not found!");
            return;
        }
        file.set("players." + player.getName() + ".color", color.toString());
        QuickNameFile.save();
    }

    public static void createPrefix(String id, String text, NamedTextColor color, boolean squareBrackets) {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("quickName.yml was not found!");
            return;
        }
        file.set("prefix." + id + ".text", text);
        file.set("prefix." + id + ".color", color.toString());
        file.set("prefix." + id + ".squarebrackets", squareBrackets);
        QuickNameFile.save();
    }

    public static Component getPrefix(Player player) {
        FileConfiguration file = QuickNameFile.get();
        if (!(file.contains("players." + player.getName() + ".prefix")) && file.get("players." + player.getName() + ".prefix") == null) {
            return Component.empty();
        }
        String prefixID = file.getString("players." + player.getName() + ".prefix");
        if (!file.contains("prefix." + prefixID)) {
            return Component.empty();
        }
        String text = file.getString("prefix." + prefixID + ".text");
        NamedTextColor color = parseColor(file.getString("prefix." + prefixID + ".color"));
        boolean squareBrackets = file.getBoolean("prefix." + prefixID + ".squarebrackets");
        if (text == null || color == null) {
            return Component.empty();
        }
        Component prefixText = Component.text(text).color(color);

        Component firstBracket;
        Component secondBracket;
        if (squareBrackets) {
            firstBracket = Component.text("[").color(NamedTextColor.WHITE);
            secondBracket = Component.text("] ").color(NamedTextColor.WHITE);
        } else {
            firstBracket = Component.empty();
            secondBracket = Component.empty();
        }

        return firstBracket.append(prefixText).append(secondBracket);
    }

    public static List<String> getAllPrefixes() {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("Could not find quickName.yml");
            return Collections.emptyList();
        }
        if (!file.contains("prefix") || file.get("prefix") == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(file.getConfigurationSection("prefix").getKeys(false));
    }
}
