package org.samo_lego.mobdisguises;

import org.samo_lego.mobdisguises.config.DisguiseConfig;

import java.io.File;
import java.nio.file.Path;

public class MobDisguises {
    public static final String MOD_ID = "mobdisguises";
    public static DisguiseConfig config;
    public static File configFile;
    public static boolean LUCKPERMS_LOADED;

    /**
     * Initialises config
     * @param configDir config directory to load config from.
     */
    public static void init(Path configDir) {
        configFile = new File( configDir + "/" + MOD_ID +"_config.json");
        config = DisguiseConfig.loadConfigFile(configFile);
    }
}
