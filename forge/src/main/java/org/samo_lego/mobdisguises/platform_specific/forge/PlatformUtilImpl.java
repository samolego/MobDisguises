package org.samo_lego.mobdisguises.platform_specific.forge;

import net.minecraft.server.command.ServerCommandSource;

public class PlatformUtilImpl {
    public static boolean hasPermission(ServerCommandSource source, String permission, int fallback) {
        return source.hasPermissionLevel(fallback);
    }
}
