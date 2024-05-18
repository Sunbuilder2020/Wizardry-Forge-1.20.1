package net.sunbuilder2020.wizardry.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.items.custom.KnowledgeScroll;
import net.sunbuilder2020.wizardry.items.custom.WizardWand;

public class ModItems {
    public static final DeferredRegister<Item> Items =
            DeferredRegister.create(ForgeRegistries.ITEMS, Wizardry.MOD_ID);

    public static final RegistryObject<Item> WIZARD_WAND = Items.register("wizard_wand",
            () -> new WizardWand(new Item.Properties().setNoRepair().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> KNOWLEDGE_SCROLL = Items.register("knowledge_scroll",
            () -> new KnowledgeScroll(new Item.Properties().setNoRepair().rarity(Rarity.RARE).stacksTo(1).craftRemainder(net.minecraft.world.item.Items.PAPER)));

    public static void register(IEventBus eventBus) {
        Items.register(eventBus);
    }
}
