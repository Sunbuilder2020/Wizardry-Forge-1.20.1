package net.sunbuilder2020.wizardry.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.items.custom.KnowledgeScroll;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wizardry.MOD_ID);

    public static final RegistryObject<CreativeModeTab> WIZARDRY_ITEMS_TAB = CREATIVE_MODE_TABS.register("wizardry_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WIZARD_WAND.get()))
                    .title(Component.translatable("creative_tab.wizardry_items_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WIZARD_WAND.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> WIZARDRY_SPELLS_TAB = CREATIVE_MODE_TABS.register("wizardry_spells_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.KNOWLEDGE_SCROLL.get()))
                    .title(Component.translatable("creative_tab.wizardry_spells_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        //pOutput.accept(ModItems.KNOWLEDGE_SCROLL.get());
                    })
                    .build());

    @SubscribeEvent
    public static void fillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeModeTabs.searchTab() || event.getTab() == WIZARDRY_SPELLS_TAB.get()) {
            SpellRegistry.getEnabledSpells().stream()
                    .forEach(spell -> {
                        ItemStack stack = KnowledgeScroll.createSpellScroll(spell);

                        event.accept(stack);
                    });
        }
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
