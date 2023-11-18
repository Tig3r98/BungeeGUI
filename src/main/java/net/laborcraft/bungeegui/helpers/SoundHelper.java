package net.laborcraft.bungeegui.helpers;

import dev.simplix.protocolize.api.SoundCategory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.Sound;
import net.laborcraft.bungeegui.BungeeGUI;

public class SoundHelper {

    public static void playSound(ProtocolizePlayer protocolizePlayer, String sound){
        if(sound == null || sound.equals(""))
            return;
        String[] soundString = sound.split(":");
        Sound soundEnum;
        float volume = 1;
        float pitch = 1;
        try {
            soundEnum = Sound.valueOf(soundString[0]);
            if(soundString.length == 3){
                volume = Float.parseFloat(soundString[1]);
                pitch = Float.parseFloat(soundString[2]);
            }
            protocolizePlayer.playSound(soundEnum,  SoundCategory.MASTER, volume, pitch);
        } catch (IllegalArgumentException exc) {
            BungeeGUI.getInstance().getLogger().warning("Invalid sound: " + sound);
        }
    }

}
