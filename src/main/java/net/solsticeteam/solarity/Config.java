package net.solsticeteam.solarity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.BooleanValue SCREEN_SHAKE_ENABLED = BUILDER
            .comment("Toggle screen shake effects")
            .define("screenShakeEnabled", true);

    public static final ModConfigSpec.IntValue GRACE_PERIOD_DAYS = BUILDER
            .comment("Time it takes for events to start happening upon starting a new world")
            .defineInRange("gracePeriodDays", 3, 0, Integer.MAX_VALUE);


    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
