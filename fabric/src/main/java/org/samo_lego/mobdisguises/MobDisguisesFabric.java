package org.samo_lego.mobdisguises;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.samo_lego.mobdisguises.command.DisguiseCommand;
import org.samo_lego.mobdisguises.command.MobDisguisesCommand;

import static org.samo_lego.mobdisguises.MobDisguises.LUCKPERMS_LOADED;

public class MobDisguisesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LUCKPERMS_LOADED = FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0");

        MobDisguises.init(FabricLoader.getInstance().getConfigDir());

        CommandRegistrationCallback.EVENT.register(DisguiseCommand::register);
        CommandRegistrationCallback.EVENT.register(MobDisguisesCommand::register);
    }
}
