package net.solsticeteam.solarity;

import net.solsticeteam.solarity.block.GenericBlocks;
import net.solsticeteam.solarity.item.GenericItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Solarity.MODID)
public class Solarity {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "solarity";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Solarity(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        GenericItems.register(modEventBus);
        GenericBlocks.register(modEventBus);

    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }
}
