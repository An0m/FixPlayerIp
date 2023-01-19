package me.an0m.fixplayerip;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class Main extends Plugin {

    static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        Config.loadConfig();

        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, new HandshakeListener());
        pluginManager.registerCommand(this, new ReloadConfigCmd());
    }
}
