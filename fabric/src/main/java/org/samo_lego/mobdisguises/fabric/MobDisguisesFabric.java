package org.samo_lego.mobdisguises.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.GameRules;
import org.samo_lego.mobdisguises.MobDisguises;
import org.samo_lego.mobdisguises.command.DisguiseCommand;
import org.samo_lego.mobdisguises.command.MobDisguisesCommand;

import static net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register;
import static org.samo_lego.mobdisguises.MobDisguises.DISGUISED_MOB_SPAWN_CHANCE;
import static org.samo_lego.mobdisguises.MobDisguises.LUCKPERMS_LOADED;

public class MobDisguisesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LUCKPERMS_LOADED = FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0");
        DISGUISED_MOB_SPAWN_CHANCE = register("disguisedMobSpawnChance", GameRules.Category.SPAWNING, GameRuleFactory.createIntRule(0, 0, 100));

        MobDisguises.init(FabricLoader.getInstance().getConfigDir());

        CommandRegistrationCallback.EVENT.register((dispatcher, access, environment) -> {
            DisguiseCommand.register(dispatcher);
            MobDisguisesCommand.register(dispatcher);
        });
    }
}
