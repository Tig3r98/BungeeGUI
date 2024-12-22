package net.laborcraft.bungeegui.commands;

import net.laborcraft.bungeegui.BungeeGUI;
import net.laborcraft.bungeegui.config.Configs;
import net.laborcraft.bungeegui.helpers.InventoryLauncher;
import net.laborcraft.bungeegui.helpers.StringUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;

public class PanelCommand extends Command implements TabExecutor {

    private final String panelName;
    // if this is a "hub" command this array specifies which servers this command supports
    private final String[] panelServers;

    public PanelCommand(String panelName, String commandName) {
        super(commandName);
        this.panelName = panelName;
        this.panelServers = Configs.getPanels().get(panelName).getServers();;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer){
            if(strings.length == 0 || panelServers == null){
                new InventoryLauncher().execute(panelName, (ProxiedPlayer) commandSender);
            } else if(strings.length == 1) {
                String arg = strings[0];
                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(arg);
                if(Arrays.asList(panelServers).contains(arg) && serverInfo != null){
                    //is player connected to this server
                    if(serverInfo.getPlayers().contains(commandSender)){
                        commandSender.sendMessage(TextComponent.fromLegacyText(Configs.getLang().getAlreadyConnected()));
                    } else {
                        ((ProxiedPlayer)commandSender).connect(serverInfo);
                    }
                } else {
                    commandSender.sendMessage(TextComponent.fromLegacyText(Configs.getLang().getUnknownServer()));
                    BungeeGUI.getInstance().getLogger().warning(BungeeGUI.PREFIX + commandSender.getName() + " tried to connect to unknown server: " + arg);
                }
            } else {
                commandSender.sendMessage(TextComponent.fromLegacyText(Configs.getLang().getUnknownArgs()));
            }
        } else {
            commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Only a player can run this command!"));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1 && panelServers != null) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(panelServers), new ArrayList<>());
        }
        return null;
    }

}
