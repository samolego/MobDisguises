package org.samo_lego.mobdisguises.platform_specific.fabric;

import net.minecraft.server.command.ServerCommandSource;
import org.samo_lego.mobdisguises.permission.LuckPermsHelper;

import static org.samo_lego.mobdisguises.MobDisguises.LUCKPERMS_LOADED;

public class PlatformUtilImpl {
    public static boolean hasPermission(ServerCommandSource source, String permission, int fallback) {
        return LUCKPERMS_LOADED ?
                LuckPermsHelper.hasPermission(source, permission, fallback) :
                source.hasPermissionLevel(fallback);
    }
}
