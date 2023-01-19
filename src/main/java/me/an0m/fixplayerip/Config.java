package me.an0m.fixplayerip;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Config {
    public static Configuration configuration;
    private static final String dataFolder = "plugins/FixPlayerIP/";
    private static final File configFile = new File(dataFolder, "config.yml");

    public static void loadConfig() {
        setupConfig();
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupConfig() {
        File folder = new File(dataFolder);

        if (!folder.exists()) {
            try {
                folder.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!configFile.exists()) {
            try {
                Files.copy(Main.instance.getResourceAsStream("config.yml"), configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
