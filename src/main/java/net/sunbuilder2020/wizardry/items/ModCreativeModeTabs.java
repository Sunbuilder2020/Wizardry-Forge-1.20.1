package net.sunbuilder2020.wizardry.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sunbuilder2020.wizardry.Wizardry;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wizardry.MOD_ID);

    public static final RegistryObject<CreativeModeTab> WIZARDRY_TAB = CREATIVE_MODE_TABS.register("wizardry_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WIZARD_WAND.get()))
                    .title(Component.translatable("creativetab.wizardry_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WIZARD_WAND.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
