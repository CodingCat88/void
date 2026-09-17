package coding.cat.voidmod.block;

import coding.cat.voidmod.VoidsReturn;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class VoidBlocks {

    public static final Block VOID_STONE = registerBlock("void_stone",
            new Block(AbstractBlock.Settings.create().strength(4.0F)
                    .requiresTool().sounds(BlockSoundGroup.ANCIENT_DEBRIS)));


    private static Block registerBlock(String name, Block block) {
        registerBlock(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(VoidsReturn.MOD_ID, name), block);
    }
    private static void registerBlockItem(String  name, Block block) {
        Registry.register(Registries.ITEM, Identifier.tryParse(name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerVoidBlocks() {
        VoidsReturn.LOGGER.info("Registering VoidBlocks For" + VoidsReturn.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(VoidBlocks.VOID_STONE);
        });
    }
}
