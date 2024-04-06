package net.sunbuilder2020.wizardry.spells;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;

import java.util.List;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID)
public class SpellsEvents {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide){
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
                    ModMessages.sendToClient(new SpellsDataSyncS2CPacket(spells.getSpells()), player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSpellsProvider.PLAYER_SPELLS).isPresent()) {
                event.addCapability(new ResourceLocation(Wizardry.MOD_ID, "properties"), new PlayerSpellsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player original = event.getOriginal();
            Player clone = event.getEntity();

            original.reviveCaps();
            original.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(oldSpells -> {
                clone.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(newSpells -> {
                    newSpells.copyFrom(oldSpells);
                });
            });

            original.invalidateCaps();
        }
    }
}
