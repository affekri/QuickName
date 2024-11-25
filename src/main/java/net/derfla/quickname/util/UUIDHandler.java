package net.derfla.quickname.util;

import java.util.UUID;

/**
 * This class handles UUIDs.
 * The method trim converts normal UUIDs to ones without dashes, which are considered trimmed.
 */
public class UUIDHandler {

    /**
     * The method converts normal UUIDs to ones without dashes, which are considered trimmed.
     * @return Input UUID as string with all "-" removed.
     */
    public static String trim(UUID uuid) {
        return String.valueOf(uuid).replaceAll("-", "");
    }
}
