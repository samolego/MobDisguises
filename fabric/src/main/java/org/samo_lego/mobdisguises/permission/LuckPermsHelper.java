package org.samo_lego.mobdisguises.permission;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;

public class LuckPermsHelper {
    public static boolean hasPermission(ServerCommandSource source, String permission, int fallbackLevel) {
        return Permissions.check(source, permission, fallbackLevel);
    }
}
