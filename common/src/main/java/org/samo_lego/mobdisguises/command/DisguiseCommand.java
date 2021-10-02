package org.samo_lego.mobdisguises.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.samo_lego.mobdisguises.platform_specific.PlatformUtil;
import xyz.nucleoid.disguiselib.casts.EntityDisguise;

import java.util.Collection;

import static com.mojang.brigadier.arguments.StringArgumentType.word;
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
                                    .then(argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                        .executes(DisguiseCommand::setDisguise)
                                    )
                            )
                            .then(literal("minecraft:player")
                                    .then(argument("playername", word())
                                            .executes(DisguiseCommand::disguiseAsPlayer)
                                    )
                                    .executes(DisguiseCommand::disguiseAsPlayer)
                            )
                            .then(literal("player")
                                    .then(argument("playername", word())
                                            .executes(DisguiseCommand::disguiseAsPlayer)
                                    )
                                    .executes(DisguiseCommand::disguiseAsPlayer)
                            )
                        )
                        .then(literal("clear").executes(DisguiseCommand::clearDisguise))
                )
        );
    }

    private static int disguiseAsPlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(ctx, "target");
        ServerCommandSource src = ctx.getSource();
        GameProfile profile;
        ServerPlayerEntity player = src.getPlayer();
        String playername;
        try {
            playername = StringArgumentType.getString(ctx, "playername");
        } catch(IllegalArgumentException ignored) {
            playername = player.getGameProfile().getName();
        }

        profile = new GameProfile(player.getUuid(), playername);
        SkullBlockEntity.loadProperties(profile, gameProfile -> {
            // Minecraft doesn't allow "summoning" players, that's why we make an exception
            GameProfile finalProfile = gameProfile == null ? player.getGameProfile() : gameProfile;
            entities.forEach(entity -> {
                if(entity == src.getEntity()) {
                    if(PlatformUtil.hasPermission(src, config.perms.disguiseSelf, config.perms.disguiseLevel)) {
                        ((EntityDisguise) entity).disguiseAs(PLAYER);
                        if(finalProfile != null) {
                            ((EntityDisguise) entity).setGameProfile(finalProfile);
                        }
                    }
                    else
                        src.sendError(NO_PERMISSION_ERROR);
                } else {
                    if(PlatformUtil.hasPermission(src, config.perms.disguiseOthers, config.perms.disguiseLevel)) {
                        ((EntityDisguise) entity).disguiseAs(PLAYER);
                        if(finalProfile != null) {
                            ((EntityDisguise) entity).setGameProfile(finalProfile);
                        }
                        src.sendFeedback(new LiteralText(config.lang.disguiseSet).formatted(Formatting.GREEN), false);
                    }
                    else
                        src.sendError(NO_PERMISSION_ERROR);
                }
            });
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
                if(PlatformUtil.hasPermission(src, config.perms.disguiseOthersClear, config.perms.disguiseLevel)) {
                    ((EntityDisguise) entity).removeDisguise();
                    src.sendFeedback(new LiteralText(config.lang.disguiseCleared).formatted(Formatting.GREEN), false);
                } else
                    src.sendError(NO_PERMISSION_ERROR);
            }
        });
        return 0;
    }

    private static int setDisguise(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(ctx, "target");
        ServerCommandSource src = ctx.getSource();
        Identifier disguise = EntitySummonArgumentType.getEntitySummon(ctx, "disguise");

        NbtCompound nbt;
        try {
            nbt = NbtCompoundArgumentType.getNbtCompound(ctx, "nbt").copy();
        } catch(IllegalArgumentException ignored) {
            nbt = new NbtCompound();
        }
        nbt.putString("id", disguise.toString());

        NbtCompound finalNbt = nbt;
        entities.forEach(entity -> EntityType.loadEntityWithPassengers(finalNbt, ctx.getSource().getWorld(), (entityx) -> {
            if(entity == src.getEntity()) {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseSelf, config.perms.disguiseLevel))
                    ((EntityDisguise) entity).disguiseAs(entityx);
                else
                    src.sendError(NO_PERMISSION_ERROR);
            } else {
                if(PlatformUtil.hasPermission(src, config.perms.disguiseOthers, config.perms.disguiseLevel)) {
                    ((EntityDisguise) entity).disguiseAs(entityx);
                    src.sendFeedback(new LiteralText(config.lang.disguiseSet).formatted(Formatting.GREEN), false);
                } else
                    src.sendError(NO_PERMISSION_ERROR);
            }
            return entityx;
        }));
        return 0;
    }
}
