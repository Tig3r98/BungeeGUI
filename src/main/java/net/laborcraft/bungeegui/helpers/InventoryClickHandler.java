package net.laborcraft.bungeegui.helpers;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import net.laborcraft.bungeegui.BungeeGUI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class InventoryClickHandler {

    /**
     * Handle the gui commands
     * @param commands
     * @param click
     */
    public void execute(String[] commands, InventoryClick click) {
        ProxiedPlayer player = BungeeGUI.getInstance().getProxy().getPlayer(click.player().uniqueId());
        for(String command : commands) {
            String[] splitCommand = command.split("=");
            switch(splitCommand[0]) {
                case "open":
                    new InventoryLauncher().execute(splitCommand[1], player);
                    break;
                case "close":
                    click.player().closeInventory();
                    break;
                case "psudo":
                    if(splitCommand[1] != null)
                        ProxyServer.getInstance().getPluginManager().dispatchCommand(player, splitCommand[1]);
                    break;
                case "server":
                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(splitCommand[1].replace(" ", ""));
                    if(serverInfo == null) {
                        BungeeGUI.getInstance().getLogger().warning(player.getName() + " tried to connect to unknown server: " + splitCommand[1].replace(" ", ""));
                    } else {
                        player.connect(serverInfo);
                    }
                    break;
                case "sound":
                    ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(player.getUniqueId());
                    SoundHelper.playSound(protocolizePlayer, splitCommand[1]);
            }
        }
    }

}
