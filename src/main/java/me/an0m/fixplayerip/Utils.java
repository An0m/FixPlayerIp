package me.an0m.fixplayerip;

import net.md_5.bungee.api.connection.PendingConnection;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import me.an0m.mcutils.ReflectionUtil;

import static me.an0m.mcutils.MsgUtils.warnBungee;

public class Utils {

    public static void blockConnection(PendingConnection connection, String colosseumProxyIP, String rawLoginAddress) {
        connection.disconnect();
        warnBungee("[FixPlayerIP] Handshake blocked!\n - Last relay (IP): \"" + colosseumProxyIP + "\"\n - Raw: \"" + rawLoginAddress + "\"");
    }

    /**
     * Clears the socket address string (/8.8.8.8:1234)
     * @param address The raw socket address string
     * @return The cleared address (8.8.8.8)
     */
    public static String clearHostName(String address) {
        return address.split("/")[1].split(":")[0];
    }

    /**
     * Changes the ip of a player using reflection.
     * Code taken (and adapted) from
     * <a href="https://github.com/TCPShield/RealIP/blob/master/src/main/java/net/tcpshield/tcpshield/bungee/handler/BungeePlayer.java">TCPShield's RealIP</a>
     *
     * @param ip The new ip
     * @param pendingConnection The instance of the connection of the player
     * @throws Exception If unable to change the ip of the player
     */
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
