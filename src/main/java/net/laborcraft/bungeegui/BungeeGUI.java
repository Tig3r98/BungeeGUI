package net.laborcraft.bungeegui;

import lombok.Getter;
import net.laborcraft.bungeegui.commands.CommandHandler;
import net.laborcraft.bungeegui.commands.PanelCommand;
import net.laborcraft.bungeegui.config.Configs;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

public class BungeeGUI extends Plugin {

    public static final String PREFIX = "&a[BungeeGUI] ".replace('&', ChatColor.COLOR_CHAR);
    @Getter private static BungeeGUI instance;

    @Override
    public void onEnable() {

        instance = this;

        Configs.loadConfigs();

        //Setup command flow
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandHandler());
        //Register command aliases for panels
        Configs.getPanels().forEach((name, panel) -> {
            String[] commands = panel.getCommands();
            if(commands == null)
                return;
            for(String command : commands) {
                ProxyServer.getInstance().getPluginManager().registerCommand(this, new PanelCommand(panel.getName(), command));
            }
        });

        Metrics metrics = new Metrics(this, 20327);

    }

    @Override
    public void onDisable(){
        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);
    }
}
