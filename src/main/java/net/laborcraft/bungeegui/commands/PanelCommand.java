package net.laborcraft.bungeegui.commands;

import net.laborcraft.bungeegui.BungeeGUI;
import net.laborcraft.bungeegui.helpers.InventoryLauncher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PanelCommand extends Command {

    private final String panelName;

    public PanelCommand(String panelName, String commandName){
        super(commandName);
        this.panelName = panelName;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer){
            new InventoryLauncher().execute(panelName, (ProxiedPlayer) commandSender);
        } else {
            commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Only a player can run this command!"));
        }
    }
}
