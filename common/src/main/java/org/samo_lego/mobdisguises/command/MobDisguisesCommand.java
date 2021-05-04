package org.samo_lego.mobdisguises.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.samo_lego.mobdisguises.config.DisguiseConfig;
import org.samo_lego.mobdisguises.platform_specific.PlatformUtil;

import static net.minecraft.server.command.CommandManager.literal;
import static org.samo_lego.mobdisguises.MobDisguises.*;

public class MobDisguisesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(literal(MOD_ID)
            .requires(src -> PlatformUtil.hasPermission(src, config.perms.mobdisguissePermissionNode, config.perms.mobdisguisesLevel))
            .then(literal("reloadConfig")
                .executes(MobDisguisesCommand::reloadConfigFile)
            )
        );
    }

    private static int reloadConfigFile(CommandContext<ServerCommandSource> ctx) {
        config = DisguiseConfig.loadConfigFile(configFile);

        ctx.getSource().sendFeedback(
                new LiteralText(config.lang.configReloaded).formatted(Formatting.GREEN),
                false
        );
        return 0;
    }
}
