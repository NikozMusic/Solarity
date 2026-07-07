package net.solsticeteam.solarity;

import net.neoforged.fml.config.ModConfig;
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

@Mod(Solarity.MODID)
public class Solarity {
    public static final String MODID = "solarity";
    public static final Logger LOGGER = LogUtils.getLogger();

    //Main Class
    public Solarity(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);


        // Register Content
        GenericItems.register(modEventBus);
        GenericBlocks.register(modEventBus);


        //Load mod config, make new config file if the TOML doesn't exist
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }
}
