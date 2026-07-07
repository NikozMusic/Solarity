package net.solsticeteam.solarity.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.solsticeteam.solarity.Solarity;
import net.solsticeteam.solarity.item.GenericItems;

import java.util.function.Supplier;

import static net.minecraft.world.item.Items.registerBlock;

public class GenericBlocks {

    /*
    * This file is for GENERIC BLOCKS. generic blocks are blocks that have no special code in them
    * any block that has some kind of unique functionality has to have its own java class file
    * */

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Solarity.MODID); // This defines what a block is for the rest of the class

    //GARDEN BUSH
    public static final DeferredBlock<Block> GARDEN_BUSH = registerBlock("garden_bush",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.AZALEA_LEAVES)));

    //YELLOW FLOWER BUSH
    public static final DeferredBlock<Block> YELLOW_BUSH = registerBlock("yellow_bush",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.AZALEA_LEAVES)));

    //CYAN FLOWER BUSH
    public static final DeferredBlock<Block> CYAN_BUSH = registerBlock("cyan_bush",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.AZALEA_LEAVES)));

    //MAGENTA FLOWER BUSH
    public static final DeferredBlock<Block> MAGENTA_BUSH = registerBlock("magenta_bush",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .sound(SoundType.AZALEA_LEAVES)));







    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    //This generates the items for each of the blocks this file makes
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        GenericItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    //This attaches the registered blocks to the main file
    public static void register(IEventBus eventBus) {BLOCKS.register(eventBus);}

}
