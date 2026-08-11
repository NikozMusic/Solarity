package net.solsticeteam.solarity;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Solarity.MODID)
public class SolarityCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("solarity")
                        .requires(source -> source.hasPermission(2)) // op-only, this is a debug command
                        .then(Commands.literal("unlock")
                                .then(Commands.argument("entryId", StringArgumentType.word())
                                        .executes(SolarityCommands::unlockEntry)))
        );
    }

    private static int unlockEntry(CommandContext<CommandSourceStack> ctx) {
        String entryId = StringArgumentType.getString(ctx, "entryId");
        CommandSourceStack source = ctx.getSource();

        LoreEntries.unlock(source.getServer(), entryId);

        source.sendSuccess(() -> Component.literal("Unlocked lore entry: " + entryId), true);
        return 1;
    }
}