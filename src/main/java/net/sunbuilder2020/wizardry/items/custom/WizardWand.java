package net.sunbuilder2020.wizardry.items.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;

public class WizardWand extends Item {
    public WizardWand(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
            AbstractSpell activeSpell = SpellRegistry.getActiveSpell(player);

            if (activeSpell != null) {
                activeSpell.castSpell((ServerPlayer) player);
            }

        });

        return super.use(level, player, interactionHand);
    }
}