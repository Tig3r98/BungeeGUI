package net.laborcraft.bungeegui.helpers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LuckPermsHelper {

    public static String getMeta(ProxiedPlayer player, String queryOption) {
        if(!doesClassExist("net.luckperms.api.LuckPerms")) {
            return null;
        }

        LuckPerms api = LuckPermsProvider.get();
        String metaValue = api.getPlayerAdapter(ProxiedPlayer.class).getMetaData(player).getMetaValue(queryOption);
        return metaValue == null ? "" : metaValue;
    }

    /**
     * Checks if a class exists or not
     * @param name
     * @return
     */
    private static boolean doesClassExist(String name) {
        try {
            Class c = Class.forName(name);
            if (c != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {}
        return false;
    }

}
