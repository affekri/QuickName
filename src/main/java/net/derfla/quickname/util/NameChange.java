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

/**
 * This class is used for basically all prefix/dolor logic of the plugin.
 */
public class NameChange {

    static Plugin plugin = QuickName.getInstance();
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * This method sets the custom display name for the supplied player. This should be used whenever a player joins or the display name changes.
     * @param player Supply the player to update the name for.
     */
    public static void setCustomDisplayName(Player player) {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("QuickName.yml was not found!");
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

    /**
     * This method return the supplied players custom name color.
     * @param player The player to get the color for.
     * @return Returns the NamedTextColor that the player has assigned.
     */
    public static NamedTextColor getColor(Player player) {
        FileConfiguration file = QuickNameFile.get();
        String trimmedUUID = UUIDHandler.trim(player.getUniqueId());
        if (file == null) {
            plugin.getLogger().severe("quickName.yml was not found!");
            return null;
        }
        if (!file.contains("players." + trimmedUUID + ".color")) return null;
        if (file.get("players." + trimmedUUID + ".color") == null) return null;
        NamedTextColor color = parseColor(file.getString("players." + trimmedUUID + ".color"));

        return color;
    }

    /**
     * This method tries to parse the input string into a NamedTextColor.
     * @param string Supply non-parsed text color.
     * @return Returns the parsed text color.
     */
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

    /**
     * This method saves the players new custom name-color to the file.
     * @param player Player to update the color for.
     * @param color What the color should be.
     */
    public static void saveToFile(Player player, NamedTextColor color) {
        FileConfiguration file = QuickNameFile.get();
        String trimmedUUID = UUIDHandler.trim(player.getUniqueId());
        if (file == null) {
            plugin.getLogger().severe("QuickName.yml was not found!");
            return;
        }
        file.set("players." + trimmedUUID + ".color", color.toString());
        file.set("players." + trimmedUUID + ".name", player.getName());
        QuickNameFile.save();
    }

    /**
     * Method to create a new or update a prefix
     * @param id The id for the prefix. Will create a new prefix if the id doesn't exist.
     * @param text The display name for the prefix. Updatable.
     * @param color The color for the prefix display name. Updatable.
     * @param squareBrackets Whether the prefix should be surrounded with square brackets or not, like [PREFIX]. Updatable.
     */
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

    /**
     * A method to get a players assigned prefix. Reads from file QuickName.yml.
     * @param player The player to get the prefix for.
     * @return Return a Component with the formated prefix.
     */
    public static Component getPrefix(Player player) {
        FileConfiguration file = QuickNameFile.get();
        String trimmedUUID = UUIDHandler.trim(player.getUniqueId());
        if (!(file.contains("players." + trimmedUUID + ".prefix")) && file.get("players." + trimmedUUID + ".prefix") == null) {
            return Component.empty();
        }
        String prefixID = file.getString("players." + trimmedUUID + ".prefix");
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

    /**
     * A method to get a list of all saved prefixes. Reads from the file QuickName.yml
     * @return A list of all prefixes in the file.
     */
    public static List<String> getAllPrefixes() {
        FileConfiguration file = QuickNameFile.get();
        if (file == null) {
            plugin.getLogger().severe("Could not find QuickName.yml");
            return Collections.emptyList();
        }
        if (!file.contains("prefix") || file.get("prefix") == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(file.getConfigurationSection("prefix").getKeys(false));
    }
}
