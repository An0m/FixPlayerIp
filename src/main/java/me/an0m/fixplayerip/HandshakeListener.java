package me.an0m.fixplayerip;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import static me.an0m.fixplayerip.Utils.*;
import static me.an0m.mcutils.MsgUtils.logBungee;

public class HandshakeListener implements Listener {

    @EventHandler(priority = 64)
    public void onHandShake(PlayerHandshakeEvent e) {
        PendingConnection connection = e.getConnection();
        boolean debugEnabled = Main.bungeeConfig.configuration.getBoolean("debug");


        String lastRelayIP = clearHostName(connection.getSocketAddress().toString());

        // * Check if the (direct) connection comes from a whitelisted ip
        if (Main.bungeeConfig.configuration.getStringList("directIPs").contains(lastRelayIP)) {
            if (debugEnabled) logBungee("[FixPlayerIP] Direct handshake allowed from " + lastRelayIP);
            return;
        }


        String rawLoginAddress = connection.getVirtualHost().getHostName();

        // * Validate the source of the connection (ip)
        if (!Main.bungeeConfig.configuration.getStringList("proxies").contains(lastRelayIP)) {
            blockConnection(connection, lastRelayIP, rawLoginAddress);
            return;
        }

        String[] _loginAddress = rawLoginAddress.split("///"); // mc.example.com///8.8.8.8:41854
        String loginDomain = _loginAddress[0];
        String loginAddress = _loginAddress[1];

        // Fixes a bug that moves the last letter of the domain extension at the end of the port
        if (rawLoginAddress.matches(".*[0-9][a-z]")) {
            loginDomain += rawLoginAddress.substring(rawLoginAddress.length() -1); // Add the first letter to the domain
            loginAddress = loginAddress.substring(0, loginAddress.length() -1); // Remove it from the port
        }


        // * Validate the source of the connection (domain)
        String domainRegex = Main.bungeeConfig.configuration.getString("domainRegex");
        boolean usingRegex = !domainRegex.isEmpty();
        if (
                (usingRegex && !loginDomain.matches(domainRegex)) ||
                (!usingRegex && !Main.bungeeConfig.configuration.getStringList("domains").contains(loginDomain))
        ) {
            if (debugEnabled) blockConnection(connection, lastRelayIP, rawLoginAddress);
            else connection.disconnect();
            return;
        }


        // Get info about the connection
        String[] _playerHostname = loginAddress.split(":");
        String playerIP = _playerHostname[0];
        int playerPort = Integer.parseInt(_playerHostname[1]);


        // * Change the ip of the player to match the original one
        try {
            setPlayerIP(new InetSocketAddress(InetAddress.getByName(playerIP), playerPort), connection);
        } catch (Exception exception) {
            blockConnection(connection, lastRelayIP, rawLoginAddress);
            if (debugEnabled) return;
        }

        if (debugEnabled)
            logBungee("[FixPlayerIP] Handshake allowed from \"" + loginAddress + "\" using \"" + loginDomain + "\"(" + lastRelayIP + ")");
    }

}
