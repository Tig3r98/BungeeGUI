package net.laborcraft.bungeegui.commands;

import net.laborcraft.bungeegui.BungeeGUI;
import net.laborcraft.bungeegui.config.Configs;
import net.laborcraft.bungeegui.helpers.InventoryLauncher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;

public class CommandHandler extends Command implements TabExecutor {


    /**
     * /bgui command
     * Lists panels and reloads the config
     */
    public CommandHandler() {
        super("bgui");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(!commandSender.hasPermission("bgui.admin")){
            commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "You don't have permission!"));
            return;
        }
        if(strings.length < 1){
            //about
            commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "BungeeGUI by Tig3r, forked from VelocityGUI by james090500"));
        } else {
            switch (strings[0]) {
                case "panel": panel(commandSender, strings); break;
                case "reload": reload(commandSender); break;
                default: commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Unknown arguments."));
            }
        }
    }

    private void panel(CommandSender commandSender, String[] strings) {
        if(strings.length < 2){
            //send list of panels
            commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Available panels"));
            Configs.getPanels().forEach((title, panel) -> {
                //Hide panels with no permissions
                if(panel.getPerm().equalsIgnoreCase("default") || commandSender.hasPermission(panel.getPerm())) {
                    commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + title));
                }
            });
            return;
        }
        if(commandSender instanceof ProxiedPlayer) {
            new InventoryLauncher().execute(strings[1], (ProxiedPlayer) commandSender);
        } else {
            commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Only a player can run this command!"));
        }
    }

    /**
     * Reloads the configs
     * @param commandSender
     */
    public void reload(CommandSender commandSender) {
        Configs.loadConfigs();
        commandSender.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Reloaded"));
        BungeeGUI.getInstance().getLogger().info("BungeeGUI Reloaded");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if(strings.length == 1) return Arrays.asList("panel", "reload");
        if(strings.length == 2 && strings[0].equals("panel")) return Configs.getPanels().keySet();
        return null;
    }
}
