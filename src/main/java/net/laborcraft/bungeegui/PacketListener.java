package net.laborcraft.bungeegui;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.PlayerInteract;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.laborcraft.bungeegui.helpers.InventoryLauncher;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.querz.nbt.tag.CompoundTag;

public class PacketListener extends AbstractPacketListener<PlayerInteract> {
    public PacketListener() {
        super(PlayerInteract.class, Direction.UPSTREAM, 0);
    }

    public void packetReceive(PacketReceiveEvent<PlayerInteract> event) {
        CompoundTag publicBukkitValues = event.packet().currentItem().nbtData().getCompoundTag("PublicBukkitValues");
        if(publicBukkitValues == null) return;
        String panel = publicBukkitValues.getString("BungeeGUI:panel");
        if(panel != null)
            new InventoryLauncher().execute(panel, (ProxiedPlayer) event.player());

    }

    public void packetSend(PacketSendEvent<PlayerInteract> packetSendEvent) {
    }
}