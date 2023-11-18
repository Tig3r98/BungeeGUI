package net.laborcraft.bungeegui.helpers;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import net.laborcraft.bungeegui.BungeeGUI;
import net.laborcraft.bungeegui.config.Configs;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class InventoryLauncher {

    /**
     * Launches an inventory instance from a panel
     * @param panelName
     * @param player
     */
    public void execute(String panelName, ProxiedPlayer player) {
        ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(player.getUniqueId());

        Configs.Panel panel = Configs.getPanels().get(panelName);
        if(panel == null) {
            protocolizePlayer.closeInventory();
            player.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Panel not found"));
            return;
        }

        //Stop players with no permissions
        if(!panel.getPerm().equalsIgnoreCase("default") && !player.hasPermission(panel.getPerm())) {
            protocolizePlayer.closeInventory();
            player.sendMessage(TextComponent.fromLegacyText(BungeeGUI.PREFIX + "Panel not found"));
            return;
        }

        protocolizePlayer.registeredInventories().clear();

        InventoryBuilder inventoryBuilder = new InventoryBuilder(BungeeGUI.getInstance(), player);
        inventoryBuilder.setRows(panel.getRows());
        inventoryBuilder.setTitle(panel.getTitle());
        inventoryBuilder.setEmpty(panel.getEmpty());
        inventoryBuilder.setItems(panel.getItems());
        Inventory inventory = inventoryBuilder.build();
        inventory.onClick(click -> {
            try {
                Configs.Item item = panel.getItems().get(click.slot());
                if(item != null && item.getCommands() != null) {
                    new InventoryClickHandler().execute(item.getCommands(), click);
                } else if(click.slot() >= 0 && click.slot() < 9*inventoryBuilder.getRowsNumber() && panel.getEmptysound() != null){
                    //play sound if config is set to
                    SoundHelper.playSound(protocolizePlayer, panel.getEmptysound());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //without this, the player would be able to retain the item on exception!
                click.cancelled(true);
            }
        });

        protocolizePlayer.openInventory(inventory);

        if(panel.getSound() != null) {
            SoundHelper.playSound(protocolizePlayer, panel.getSound());
        }
    }
}
