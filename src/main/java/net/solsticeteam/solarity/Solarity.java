package net.solsticeteam.solarity;

import net.neoforged.fml.config.ModConfig;
import net.solsticeteam.solarity.block.GenericBlocks;
import net.solsticeteam.solarity.item.GenericItems;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Mod(Solarity.MODID)
public class Solarity {
    public static final String MODID = "solarity";
    public static final Logger LOGGER = LogUtils.getLogger();


    private ScheduledExecutorService heartbeatExecutor;

    public Solarity(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        GenericItems.register(modEventBus);
        GenericBlocks.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        File solarityDir = new File(FMLPaths.GAMEDIR.get().toFile(), "solarity");
        solarityDir.mkdirs();
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();

        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "solarity-heartbeat");
            t.setDaemon(true);
            return t;
        });
        heartbeatExecutor.scheduleAtFixedRate(() -> writeHeartbeat(server), 0, 5, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        if (heartbeatExecutor != null) {
            heartbeatExecutor.shutdownNow();
            heartbeatExecutor = null;
        }
        File file = new File(FMLPaths.GAMEDIR.get().toFile(), "solarity/session.json");
        file.delete();
    }

    private void writeHeartbeat(MinecraftServer server) {
        try {
            File file = new File(FMLPaths.GAMEDIR.get().toFile(), "solarity/session.json");
            String worldName = server.getWorldData().getLevelName();
            String saveDir = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT)
                    .toAbsolutePath().toString().replace("\\", "\\\\");
            String json = "{\"world\":\"" + worldName + "\",\"savePath\":\"" + saveDir
                    + "\",\"timestamp\":" + System.currentTimeMillis() + "}";
            try (FileWriter w = new FileWriter(file)) {
                w.write(json);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to write Solarity heartbeat", e);
        }
    }
}