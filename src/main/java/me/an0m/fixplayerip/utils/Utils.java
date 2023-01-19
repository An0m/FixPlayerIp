package me.an0m.fixplayerip.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Utils {

    public static String cc(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(cc(message));
    }

    public static void blockConnection(PendingConnection connection, String colosseumProxyIP, String rawLoginAddress) {
        connection.disconnect();
        warn("[FixPlayerIP] Handshake blocked!\n - Last relay (IP): \"" + colosseumProxyIP + "\"\n - Raw: \"" + rawLoginAddress + "\"");
    }

    public static void warn(String msg) {
        ProxyServer.getInstance().getLogger().warning(msg);
    }

    public static void log(String msg) {
        sendMessage(ProxyServer.getInstance().getConsole(), msg);
    }

    public static String clearHostName(String address) {
        return address.split("/")[1].split(":")[0];
    }

    public static void setPlayerIP(InetSocketAddress ip, PendingConnection pendingConnection) throws Exception {
        try {
            Object channelWrapper = ReflectionUtil.getObjectInPrivateField(pendingConnection, "ch");
            Object channel = ReflectionUtil.getObjectInPrivateField(channelWrapper, "ch");

            // Change the socket field
            try {
                Field socketAddressField = ReflectionUtil.searchFieldByClass(channelWrapper.getClass(), SocketAddress.class);
                ReflectionUtil.setFinalField(channelWrapper, socketAddressField, ip);
            } catch (IllegalArgumentException ignored) {
                // Some BungeeCord versions, notably those on 1.7 (e.g. zBungeeCord) don't have an SocketAddress field in the ChannelWrapper class
            }

            // Change the remote address field
            ReflectionUtil.setFinalField(channel, ReflectionUtil.getPrivateField(channel.getClass(), "remoteAddress"), ip);
            try {
                ReflectionUtil.setFinalField(channel, ReflectionUtil.getPrivateField(channel.getClass(), "remote"), ip);
            } catch (IllegalArgumentException ignored) {}
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
