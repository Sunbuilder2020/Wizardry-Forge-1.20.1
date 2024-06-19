package net.sunbuilder2020.wizardry.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.CastingDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerCastingProvider;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellCooldownsProvider;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

public class WizardWand extends Item {
    public WizardWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            serverPlayer.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
                serverPlayer.getCapability(PlayerSpellCooldownsProvider.PLAYER_SPELL_COOLDOWNS).ifPresent(spellCooldowns -> {
                    AbstractSpell activeSpell = playerSpells.getActiveSpell(playerSpells.getActiveSpellSlot());

                    if (activeSpell != null && !(activeSpell instanceof NoneSpell) && !spellCooldowns.isOnCooldown(activeSpell)) {
                        activeSpell.startCast(serverPlayer);

                        player.startUsingItem(hand);
                    }
                });
            });
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!level.isClientSide && entity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) entity;

            player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(playerCasting -> {
                playerCasting.stopCasting();
                ModMessages.sendToClient(new CastingDataSyncS2CPacket(SpellRegistry.noneSpell(), 0), player);
            });
        }

        super.releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!level.isClientSide && entity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) entity;
            player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(playerCasting -> {
                if (playerCasting.isCasting()) {
                    playerCasting.tick(player);

                    if (!playerCasting.isCasting()) {
                        entity.stopUsingItem();
                    }
                }
            });
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Duration for which the item can be used (in ticks)
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW; // Animations to use for the item
    }
}
