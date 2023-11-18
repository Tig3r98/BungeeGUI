package net.laborcraft.bungeegui.helpers;

import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.item.BaseItemStack;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;
import lombok.AccessLevel;
import lombok.Getter;
import net.laborcraft.bungeegui.BungeeGUI;
import net.laborcraft.bungeegui.config.Configs;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.querz.nbt.tag.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class InventoryBuilder {

    @Getter(AccessLevel.NONE) private BungeeGUI bungeeGUI;
    private ProxiedPlayer player;
    private InventoryType rows;
    private BaseComponent[] title;
    private List<BaseItemStack> emptyItems = new ArrayList<>();
    private HashMap<Integer, ItemStack> items = new HashMap<>();

    /**
     * The builder
     * @param bungeeGUI
     * @param player
     */
    public InventoryBuilder(BungeeGUI bungeeGUI, ProxiedPlayer player) {
        this.bungeeGUI = bungeeGUI;
        this.player = player;
    }

    /**
     * Sets the rows of the GUI to display
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = getInventoryType(rows);
    }

    public int getRowsNumber() {
        switch (rows) {
            case GENERIC_9X1: return 1;
            case GENERIC_9X2: return 2;
            case GENERIC_9X3: return 3;
            case GENERIC_9X4: return 4;
            case GENERIC_9X5: return 5;
            default: return 6;
        }
    }

    /**
     * Sets the title and converts a string to a Component
     * @param title
     */
    public void setTitle(String title) {
        this.title = PlaceholderParser.of(this.player, title);
    }

    /**
     * Set the empty item
     * @param item
     */
    public void setEmpty(String item) {
        ItemStack itemStack = new ItemStack(ItemType.valueOf(item));
        itemStack.displayName("");
        itemStack.amount((byte) 1);

        int totalSlots = this.getRows().getTypicalSize(player.getPendingConnection().getVersion());
        for(int i = 0; i < totalSlots; i++) {
            emptyItems.add(itemStack);
        }
    }

    /**
     * Add items to the panel
     * @param guiItems
     */
    public void setItems(HashMap<Integer, Configs.Item> guiItems) {
        guiItems.forEach((index, guiItem) -> {
            //Set the item Material, Name and Amount
            ItemStack itemStack;
            if(guiItem.getMaterial().startsWith("head=")) {
                itemStack = new ItemStack(ItemType.PLAYER_HEAD);
            } else {
                try {
                    itemStack = new ItemStack(ItemType.valueOf(guiItem.getMaterial()));
                } catch (IllegalArgumentException e) {
                    itemStack = new ItemStack(ItemType.STONE);
                    this.bungeeGUI.getLogger().severe("Invalid Material! " + guiItem.getMaterial());
                }
            }

            itemStack.displayName(PlaceholderParser.of(this.player, guiItem.getName()));
            itemStack.amount(guiItem.getStack());

            //Set any lore on the item
            if(guiItem.getLore() != null) {
                for (String lore : guiItem.getLore()) {
                    itemStack.addToLore(PlaceholderParser.of(this.player, lore));
                }
            }

            //Get the item NBT
            CompoundTag tag = itemStack.nbtData();

            //Set enchantment on the item if needed
            if(guiItem.isEnchanted()) {
                ListTag<CompoundTag> enchantments = new ListTag<>(CompoundTag.class);
                CompoundTag enchantment = new CompoundTag();
                enchantment.put("id", new StringTag("minecraft:unbreaking"));
                enchantment.put("lvl", new ShortTag((short) 1));
                enchantments.add(enchantment);
                tag.put("Enchantments", enchantments);
            }

            //If a player heads lets do this
            if(guiItem.getMaterial().startsWith("head= ")) {
                String headData = guiItem.getMaterial().replace("head= ", "");
                if(headData.equals("self")) {
                    tag.put("SkullOwner", new StringTag(player.getName()));

                } else {
                    CompoundTag skullOwnerTag = tag.getCompoundTag("SkullOwner");
                    CompoundTag propertiesTag = tag.getCompoundTag("Properties");
                    ListTag<CompoundTag> texturesTag = new ListTag<>(CompoundTag.class);
                    CompoundTag textureTag = new CompoundTag();

                    if (skullOwnerTag == null) {
                        skullOwnerTag = new CompoundTag();
                    }
                    if (propertiesTag == null) {
                        propertiesTag = new CompoundTag();
                    }

                    textureTag.put("Value", new StringTag(headData));
                    texturesTag.add(textureTag);
                    propertiesTag.put("textures", texturesTag);
                    skullOwnerTag.put("Properties", propertiesTag);
                    skullOwnerTag.put("Name", new StringTag(headData));
                    tag.put("SkullOwner", skullOwnerTag);
                }

                //Set item NBT
                itemStack.nbtData(tag);
            }

            tag.put("HideFlags", new IntTag(99));
            tag.put("overrideMeta", new ByteTag((byte)1));

            //Add to a hashmap
            items.put(index, itemStack);
        });
    }

    /**
     * Get the type of inventory by the rows
     * @param value
     * @return
     */
    private InventoryType getInventoryType(int value) {
        switch(value) {
            case 1:
                return InventoryType.GENERIC_9X1;
            case 2:
                return InventoryType.GENERIC_9X2;
            case 3:
                return InventoryType.GENERIC_9X3;
            case 4:
                return InventoryType.GENERIC_9X4;
            case 5:
                return InventoryType.GENERIC_9X5;
            default:
                return InventoryType.GENERIC_9X6;
        }
    }

    /**
     * Build the inventory
     * @return
     */
    public Inventory build() {
        Inventory inventory = new Inventory(this.getRows());
        inventory.title(this.getTitle());
        inventory.items(this.getEmptyItems());

        this.getItems().forEach((index, item) -> {
            inventory.item(index, item);
        });

        return inventory;
    }

}
