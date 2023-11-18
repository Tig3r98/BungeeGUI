package net.laborcraft.bungeegui.helpers;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlaceholderParser {

    private static final Set<String> servers = ProxyServer.getInstance().getServersCopy().keySet();

    public static @NotNull BaseComponent[] of(ProxiedPlayer player, String rawString) {
        //Username
        if(rawString.contains("%username%")) {
            rawString = rawString.replaceAll("%username%", player.getName());
        }

        //Username
        if(rawString.contains("%displayname%")) {
            rawString = rawString.replaceAll("%displayname%", player.getDisplayName());
        }

        //Server Name
        if(rawString.contains("%server_name%")) {
            rawString = rawString.replaceAll("%server_name%", player.getServer().getInfo().getName());
        }

        for(String serverName : servers){
            String placeholder = "%server_players_"+serverName+"%";
            if(rawString.contains(placeholder)) {
                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
                if(serverInfo == null){
                    rawString = rawString.replace(placeholder, String.valueOf(0));
                } else {
                    rawString = rawString.replace(placeholder, String.valueOf(serverInfo.getPlayers().size()));
                }
            }
        }

        //Server Name
        if(rawString.contains("%proxy_players%")) {
            rawString = rawString.replaceAll("%proxy_players%", String.valueOf(ProxyServer.getInstance().getPlayers().size()));
        }

        //LuckPerms Meta
        if(rawString.startsWith("%luckperms_meta")) {
            String queryOption = rawString.replaceAll("%", "").replaceAll("luckperms_meta_", "");
            LuckPermsHelper.getMeta(player, queryOption);
        }

        return BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(rawString).decoration(TextDecoration.ITALIC, false));
    }
}
