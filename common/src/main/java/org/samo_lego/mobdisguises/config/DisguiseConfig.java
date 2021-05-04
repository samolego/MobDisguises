package org.samo_lego.mobdisguises.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.samo_lego.mobdisguises.MobDisguises.MOD_ID;

public class DisguiseConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    @SerializedName("permissions")
    public Permissions perms = new Permissions();

    public static class Permissions  {
        public final  String _comment_disguisePermissions = "// Permissions for /disguise command";
        /**
         * Default permission level to use.
         */
        @SerializedName("disguise_permission_level")
        public int disguiseLevel = 2;
        @SerializedName("disguise_permission_node")
        public final String disguisePermissionNode = MOD_ID + ".disguise";
        public final String disguiseSelf = MOD_ID + ".disguise.self";
        public final String disguiseSelfClear = MOD_ID + ".disguise.self.clear";
        public final String disguiseOthers = MOD_ID + ".disguise.others";
        public final String disguiseOthersClear = MOD_ID + ".disguise.others.clear";

        public final  String _comment_mobdisguisesPermissions = "// Permissions for /mobdisguises command";
        @SerializedName("mobdisguises_permission_level")
        public int mobdisguisesLevel = 4;
        @SerializedName("mobdisguises_permission_node")
        public final String mobdisguissePermissionNode = MOD_ID;
    }

    @SerializedName("language")
    public Language lang = new Language();

    public static class Language {
        public String configReloaded = "Config was reloaded successfully.";
    }


    /**
     * Loads config file.
     *
     * @param file file to load the config file from.
     * @return config object
     */
    public static DisguiseConfig loadConfigFile(File file) {
        DisguiseConfig config;
        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = GSON.fromJson(fileReader, DisguiseConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("[MobDisguises] Problem occurred when trying to load config: ", e);
            }
        } else
            config = new DisguiseConfig();
        config.saveConfigFile(file);

        return config;
    }

    /**
     * Saves the config to the given file.
     *
     * @param file file to save config to
     */
    public void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            getLogger("MobDisguises").error("Problem occurred when saving config: " + e.getMessage());
        }
    }
}
