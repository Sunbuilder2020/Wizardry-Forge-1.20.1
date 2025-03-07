package net.sunbuilder2020.wizardry;

import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sunbuilder2020.wizardry.entity.ModEntities;
import net.sunbuilder2020.wizardry.events.ModClientEvents;
import net.sunbuilder2020.wizardry.items.ModCreativeModeTabs;
import net.sunbuilder2020.wizardry.items.ModItems;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
//Todo: Add hurt marking to Damaging spells, Better Animation/Textures/Models, Add mod Config
@Mod(Wizardry.MOD_ID)
public class Wizardry {
    public static final String MOD_ID = "wizardry";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Wizardry() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(ModClientEvents::registerRenderers);

        MinecraftForge.EVENT_BUS.register(this);

        GeckoLib.initialize();

        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        ModEntities.register(modEventBus);

        SpellRegistry.register(modEventBus);

        ModMessages.register();
    }
}
