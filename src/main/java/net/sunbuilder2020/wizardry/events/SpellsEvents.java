package net.sunbuilder2020.wizardry.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerCastingProvider;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellCooldowns;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellCooldownsProvider;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;

@Mod.EventBusSubscriber(modid = Wizardry.MOD_ID)
public class SpellsEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerSpellsProvider.PLAYER_SPELLS).isPresent()) {
                event.addCapability(new ResourceLocation(Wizardry.MOD_ID, "player_spells"), new PlayerSpellsProvider());
            }

            if (!event.getObject().getCapability(PlayerCastingProvider.PLAYER_CASTING).isPresent()) {
                event.addCapability(new ResourceLocation(Wizardry.MOD_ID, "player_casting"), new PlayerCastingProvider());
            }

            if (!event.getObject().getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).isPresent()) {
                event.addCapability(new ResourceLocation(Wizardry.MOD_ID, "player_spell_cooldowns"), new PlayerSpellCooldownsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player original = event.getOriginal();
            Player clone = event.getEntity();

            original.reviveCaps();
            original.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(oldSpells ->
                    clone.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(newSpells -> {
                        newSpells.copyFrom(oldSpells);
                        newSpells.syncData((ServerPlayer) clone);
                    })
            );

            original.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(oldCasting ->
                    clone.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(newCasting -> {
                        newCasting.copyFrom(oldCasting);
                        newCasting.syncData((ServerPlayer) clone);
                    })
            );

            original.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(oldCooldowns ->
                    clone.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(newCooldowns -> {
                        newCooldowns.copyFrom(oldCooldowns);
                        newCooldowns.syncData((ServerPlayer) clone);
                    })
            );

            original.invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> spells.syncData(player));
            player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(casting -> casting.syncData(player));
            player.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(cooldowns -> cooldowns.syncData(player));
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(PlayerSpellCooldowns::tick);
    }
}
