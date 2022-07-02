package net.spyman.backpackmod.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.spyman.backpackmod.common.BackpackMod;

import java.io.*;

import static net.spyman.backpackmod.common.BackpackMod.LOGGER;

public final class BackpackCfgFile {

    public static final BackpackCfgFile INSTANCE = new BackpackCfgFile();
    /**
     * Configuration file name
     */
    public static final String CFG_NAME = BackpackMod.MODID + ".json";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(BackpackConfig.class, new BackpackConfig.Serializer())
            .registerTypeAdapter(BackpackEntry.class, new BackpackEntry.Serializer())
            .create();
    /**
     * Config
     */
    private BackpackConfig config;

    /**
     * Config file
     */
    private File file;

    public BackpackCfgFile() {
        this.config = new BackpackConfig();
    }

    /**
     * Initialize configuration file
     */
    public void init() {
        this.file = FabricLoader.getInstance().getConfigDir().resolve(CFG_NAME).toFile();
    }

    /**
     * Called once at startup to load existing cfg, or create default cfg file
     */
    public void load() {
        try {
            if (file.exists()) {
                LOGGER.info("Configuration file found");
                this.config = GSON.fromJson(new FileReader(file), BackpackConfig.class);
            } else {
                LOGGER.info("Configuration not found, creating default configuration");
                this.config.defaultConfig();
                this.save();
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Failed to load configuration file", e);
        }
    }

    /**
     * Save the current configuration to the configuration file
     */
    public void save() {
        LOGGER.info("Configuration file successfuly written");

        var content = GSON.toJson(this.config);

        try {
            try (var w = new FileWriter(file)) {
                w.write(GSON.toJson(this.config));
            }
        } catch (IOException e) {
            LOGGER.error("Failed to write configuration file", e);
        }
    }

    public static final BackpackCfgFile instance() {
        return INSTANCE;
    }

    public static final BackpackConfig config() {
        return INSTANCE.config;
    }
}
