package org.samo_lego.mobdisguises.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.argument.NbtCompoundTagArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.samo_lego.mobdisguises.platform_specific.PlatformUtil;
import xyz.nucleoid.disguiselib.casts.EntityDisguise;

import java.util.Collection;

import static net.minecraft.command.argument.EntityArgumentType.entities;
import static net.minecraft.command.suggestion.SuggestionProviders.SUMMONABLE_ENTITIES;
import static net.minecraft.entity.EntityType.PLAYER;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static org.samo_lego.mobdisguises.MobDisguises.config;

public class DisguiseCommand {

    private static final Text NO_PERMISSION_ERROR = new TranslatableText("commands.help.failed");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(literal("disguise")
                .requires(src -> PlatformUtil.hasPermission(src, config.perms.disguisePermissionNode, config.perms.disguiseLevel))
                .then(argument("target", entities())
                        .then(literal("as")
                            .then(argument("disguise", EntitySummonArgumentType.entitySummon())
                                .suggests(SUMMONABLE_ENTITIES)
                                .executes(DisguiseCommand::setDisguise)
                                    .then(argument("nbt", NbtCompoundTagArgumentType.nbtCompound())
                                        .executes(DisguiseCommand::setDisguise)
                                    )
                            )
                            .then(literal("minecraft:player").executes(DisguiseCommand::disguiseAsPlayer))
                            .then(literal("player").executes(DisguiseCommand::disguiseAsPlayer))
                        )
                        .then(literal("clear").executes(DisguiseCommand::clearDisguise))
                )
        );
    }

    private static int disguiseAsPlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(ctx, "target");
        ServerCommandSource src = ctx.getSource();
        // Minecraft doesn't allow "summoning" players, that's why we make an exception
        entities.forEach(entity -> {
            if(entity == src.getEntity()) {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseSelf, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).disguiseAs(PLAYER);
                else
                    src.sendError(NO_PERMISSION_ERROR);
            } else {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseOthers, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).disguiseAs(PLAYER);
                else
                    src.sendError(NO_PERMISSION_ERROR);
            }
        });
        return 0;
    }

    private static int clearDisguise(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(ctx, "target");
        ServerCommandSource src = ctx.getSource();
        // Minecraft doesn't allow "summoning" players, that's why we make an exception
        entities.forEach(entity -> {
            if(entity == src.getEntity()) {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseSelfClear, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).removeDisguise();
                else
                    src.sendError(NO_PERMISSION_ERROR);
            } else {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseOthersClear, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).removeDisguise();
                else
                    src.sendError(NO_PERMISSION_ERROR);
            }
        });
        return 0;
    }

    private static int setDisguise(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(ctx, "target");
        ServerCommandSource src = ctx.getSource();
        Identifier disguise = EntitySummonArgumentType.getEntitySummon(ctx, "disguise");

        CompoundTag nbt;
        try {
            nbt = NbtCompoundTagArgumentType.getCompoundTag(ctx, "nbt").copy();
        } catch(IllegalArgumentException ignored) {
            nbt = new CompoundTag();
        }
        nbt.putString("id", disguise.toString());

        CompoundTag finalNbt = nbt;
        entities.forEach(entity -> EntityType.loadEntityWithPassengers(finalNbt, ctx.getSource().getWorld(), (entityx) -> {
            if(entity == src.getEntity()) {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseSelf, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).disguiseAs(entityx);
                else
                    src.sendError(NO_PERMISSION_ERROR);
            } else {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseOthers, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).disguiseAs(entityx);
                else
                    src.sendError(NO_PERMISSION_ERROR);
            }
            return entityx;
        }));
        return 0;
    }
}
