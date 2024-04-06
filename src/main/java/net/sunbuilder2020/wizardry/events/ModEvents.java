package net.sunbuilder2020.wizardry.events;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.AddSpellC2SPacket;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.PlayerSpells;
import net.sunbuilder2020.wizardry.spells.PlayerSpellsProvider;
import org.apache.logging.log4j.core.Logger;

import java.util.List;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID)
public class ModEvents {

}

