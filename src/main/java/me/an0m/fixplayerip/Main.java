package me.an0m.fixplayerip;

import me.an0m.mcutils.bungee.BungeeConfig;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class Main extends Plugin {

    static Plugin instance;
    public static BungeeConfig bungeeConfig;

    @Override
    public void onEnable() {
        instance = this;
        bungeeConfig = new BungeeConfig(this, "FixPlayerIP");

        bungeeConfig.loadConfig();
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, new HandshakeListener());
        pluginManager.registerCommand(this, new ReloadConfigCmd());
    }
}
