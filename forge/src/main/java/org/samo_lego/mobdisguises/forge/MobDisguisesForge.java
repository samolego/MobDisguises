package org.samo_lego.mobdisguises;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.samo_lego.mobdisguises.command.DisguiseCommand;
import org.samo_lego.mobdisguises.command.MobDisguisesCommand;

import static net.minecraft.world.GameRules.register;
import static org.samo_lego.mobdisguises.MobDisguises.DISGUISED_MOB_SPAWN_CHANCE;
import static org.samo_lego.mobdisguises.mixin.GameRulesTypeAccessor.invokeCreate;

@Mod(MobDisguises.MOD_ID)
public class MobDisguisesForge {
    public MobDisguisesForge() {
        DISGUISED_MOB_SPAWN_CHANCE = register("disguisedMobSpawnChance", GameRules.Category.SPAWNING, invokeCreate(0));
        MobDisguises.init(FMLPaths.CONFIGDIR.get());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<ServerCommandSource> dispatcher = event.getDispatcher();

        DisguiseCommand.register(dispatcher, false);
        MobDisguisesCommand.register(dispatcher, false);
    }

}
