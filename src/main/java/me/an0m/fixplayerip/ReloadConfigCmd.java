package me.an0m.fixplayerip;

import me.an0m.fixplayerip.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadConfigCmd extends Command {
    public ReloadConfigCmd() {
        super("fixplayerip", "fixplayerip.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
            Config.loadConfig();

        else Utils.sendMessage(sender, "&cUsage: /fixplayerip reload");
    }
}