package net.solsticeteam.solarity.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.solsticeteam.solarity.Solarity;

public class GenericItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Solarity.MODID); // Define what an item is




    //Register all the items
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
