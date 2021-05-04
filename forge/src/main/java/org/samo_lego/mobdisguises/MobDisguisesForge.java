package org.samo_lego.mobdisguises;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.samo_lego.mobdisguises.command.DisguiseCommand;

@Mod(MobDisguises.MOD_ID)
public class MobDisguisesForge {
    public MobDisguisesForge() {
        MobDisguises.init(FMLPaths.CONFIGDIR.get());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<ServerCommandSource> dispatcher = event.getDispatcher();

        DisguiseCommand.register(dispatcher, false);
    }

}
