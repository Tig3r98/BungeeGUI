package net.laborcraft.bungeegui.config;

import com.moandjiezana.toml.Toml;
import lombok.Getter;
import net.laborcraft.bungeegui.BungeeGUI;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class Configs {

    @Getter private static HashMap<String, Panel> panels = new HashMap<>();

    /**
     * Loads the config files.
     */
    public static void loadConfigs() {

        Path dataDirectory = new File("./plugins/BungeeGUI/").toPath();

        //Create data directory
        if(!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdir();
        }



        //Create default config
        File configFile = new File(dataDirectory + "/config.toml");
        if(!configFile.exists()) {
            try (InputStream in = BungeeGUI.class.getResourceAsStream("/config.toml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //load config
        config = new Toml().read(configFile).to(Config.class);



        //Create panel directory
        File panelDir = new File(dataDirectory.toFile() + "/panels");
        if(!panelDir.exists()) {
            panelDir.mkdir();
        }
        //load default example
        if(panelDir.listFiles().length == 0) {
            try (InputStream in = BungeeGUI.class.getResourceAsStream("/example.toml")) {
                Files.copy(in, new File(panelDir + "/example.toml").toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //load config
        for(File file : panelDir.listFiles()) {
            if(!file.getName().endsWith(".toml")) continue; //skip files without the right extension
            Panel panel = new Toml().read(file).to(Panel.class);
            panels.put(panel.getName(), panel);
        }



        //Create lang directory
        File langDir = new File(dataDirectory.toFile() + "/lang");
        if(!langDir.exists()) {
            langDir.mkdir();
        }
        //load default data
        for(String lang : new String[]{"en-us", "it-it"}){
            File langFile = new File(langDir + "/"+lang+".toml");
            if(!langFile.exists()) {
                try (InputStream in = BungeeGUI.class.getResourceAsStream("/lang/"+lang+".toml")) {
                    Files.copy(in, langFile.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //load lang
        lang = new Toml().read(new File(langDir + "/"+config.getLang()+".toml")).to(Lang.class);
        //check all keys and load missing ones, then translate all strings
        for (Field field : Lang.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType().equals(String.class)) {
                    //get the current value of the field.
                    String value = (String) field.get(lang); // Assuming static fields.
                    if(value == null){
                        BungeeGUI.getInstance().getLogger().severe("Missing lang string: \"" + field.getName()+ "\"! This will result in errors.");
                    } else {
                        //edit color chars
                        field.set(lang, value.replace('&', ChatColor.COLOR_CHAR));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        //.replace('&', ChatColor.COLOR_CHAR)

    }

    @Getter private static Config config;
    @Getter private static Lang lang;

    public class Config {
        @Getter private String lang;
    }

    public class Lang {
        @Getter private String alreadyConnected;
        @Getter private String unknownArgs;
        @Getter private String unknownServer;
    }

    public class Panel {

        @Getter private String name;
        @Getter private String perm;
        @Getter private int rows ;
        @Getter private String title;
        @Getter private String empty;
        @Getter private String sound;
        @Getter private String emptysound;
        @Getter private String[] commands;
        @Getter private HashMap<Integer, Item> items;
        @Getter private String[] servers;

        @Override
        public String toString() {
            return "Panel{" +
                    "name='" + name + '\'' +
                    ", perm='" + perm + '\'' +
                    ", rows=" + rows +
                    ", title='" + title + '\'' +
                    ", empty='" + empty + '\'' +
                    ", sound='" + sound + '\'' +
                    ", emptysound='" + emptysound + '\'' +
                    ", items=" + items +
                    ", servers=" + Arrays.toString(servers) +
                    '}';
        }
    }

    public class Item {

        private String name;
        @Getter private String material;
        private byte stack;
        @Getter private String[] lore;
        @Getter private boolean enchanted;
        @Getter private String[] commands;

        /**
         * Return name or make empty if missed from config
         * @return
         */
        public String getName() {
            return (name != null) ? name : "&f";
        }

        /**
         * If stack is missed from config make it 1
         * @return
         */
        public byte getStack() {
            return (stack > 0) ? stack : 1;
        }

        @Override
        public String toString() {
            return "GuiItem{" +
                    "name='" + name + '\'' +
                    ", material='" + material + '\'' +
                    ", stack=" + stack +
                    ", lore=" + Arrays.toString(lore) +
                    ", enchanted=" + enchanted +
                    '}';
        }
    }

}
