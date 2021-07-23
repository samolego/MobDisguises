package org.samo_lego.mobdisguises.platform_specific;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.server.command.ServerCommandSource;

public class PlatformUtil {
    @ExpectPlatform
    public static boolean hasPermission(ServerCommandSource source, String permission, int fallback) {
        throw new AssertionError();
    }
}
