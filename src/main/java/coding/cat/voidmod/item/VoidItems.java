package coding.cat.voidmod.item;

import coding.cat.voidmod.VoidsReturn;
import coding.cat.voidmod.item.custom.VoidTotemItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class VoidItems {


    public static final Item NULL_TOTEM = registerItem("totem_of_null", new VoidTotemItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));

    public static final Item VOIDSTEEL_INGOT = registerItem("voidsteel_ingot", new Item(new Item.Settings()));
    public static final Item RAW_VOIDSTEEL = registerItem("raw_voidsteel", new Item(new Item.Settings()));
    public static final Item VOIDSTEEL_UPGRADE_TEMPLATE = registerItem("voidsteel_upgrade_template", new Item(new Item.Settings()));

    public static final Item VOIDSTEEL_SWORD = registerItem("voidsteel_sword", new SwordItem(VoidToolMaterials.VOID_STEEL,
            new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(VoidToolMaterials.VOID_STEEL, 3, -2.4f))));
    public static final Item VOIDSTEEL_PICKAXE = registerItem("voidsteel_pickaxe", new PickaxeItem(VoidToolMaterials.VOID_STEEL,
            new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(VoidToolMaterials.VOID_STEEL, 1, -2.8f))));
    public static final Item VOIDSTEEL_AXE = registerItem("voidsteel_axe", new AxeItem(VoidToolMaterials.VOID_STEEL,
            new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(VoidToolMaterials.VOID_STEEL, 6, -3.2f))));
    public static final Item VOIDSTEEL_SHOVEL = registerItem("voidsteel_shovel", new ShovelItem(VoidToolMaterials.VOID_STEEL,
            new Item.Settings().attributeModifiers(ShovelItem.createAttributeModifiers(VoidToolMaterials.VOID_STEEL, 1.5f, -3.0f))));
    public static final Item VOIDSTEEL_HOE = registerItem("voidsteel_hoe", new HoeItem(VoidToolMaterials.VOID_STEEL,
            new Item.Settings().attributeModifiers(HoeItem.createAttributeModifiers(VoidToolMaterials.VOID_STEEL, 0, -3f))));

    public static final Item VOIDSTEEL_MACE = registerItem("voidsteel_mace", new MaceItem(
            new Item.Settings().attributeModifiers(MaceItem.createAttributeModifiers())));


    public static void hi() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(VOIDSTEEL_INGOT);
            entries.add(RAW_VOIDSTEEL);
            entries.add(VOIDSTEEL_UPGRADE_TEMPLATE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(VOIDSTEEL_SWORD);
            entries.add(VOIDSTEEL_AXE);
            entries.add(VOIDSTEEL_MACE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(VOIDSTEEL_PICKAXE);
            entries.add(VOIDSTEEL_SHOVEL);
            entries.add(VOIDSTEEL_HOE);
        });
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(VoidsReturn.MOD_ID, name), item);
    }
}
