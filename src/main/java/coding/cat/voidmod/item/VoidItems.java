package coding.cat.voidmod.item;

import coding.cat.voidmod.VoidsReturn;
import coding.cat.voidmod.item.custom.VoidTotemItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class VoidItems {


    public static Item NULL_TOTEM = registerItem("totem_of_null", new VoidTotemItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));


    public static void hi() {}

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(VoidsReturn.MOD_ID, name), item);
    }
}
