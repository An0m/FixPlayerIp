package me.an0m.fixplayerip;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import static me.an0m.mcutils.bungee.BungeeUtils.sendMessage;

public class ReloadConfigCmd extends Command {
    public ReloadConfigCmd() {
        super("fixplayerip", "fixplayerip.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
            Main.bungeeConfig.loadConfig();

        else sendMessage(sender, "&cUsage: /fixplayerip reload");
    }
}
