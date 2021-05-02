package org.samo_lego.mobdisguises.fabric;

import net.fabricmc.api.ModInitializer;
import org.samo_lego.mobdisguises.MobDisguises;

public class MobDisguisesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MobDisguises.init();
    }
}
