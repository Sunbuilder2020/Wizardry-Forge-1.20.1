package net.sunbuilder2020.wizardry.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.sunbuilder2020.wizardry.items.ModItems;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class KnowledgeScroll extends Item {
    public KnowledgeScroll(Properties pProperties) {
        super(pProperties);
    }

    public static void setSpellData(ItemStack itemStack, String spell) {
        CompoundTag tag = itemStack.getOrCreateTag();

        tag.putString("spell", spell);
        itemStack.setTag(tag);
    }

    public static String getSpellData(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();

        if (tag != null) {
            return tag.getString("spell");
        }

        return null;
    }

    public static Optional<AbstractSpell> getSpellClassFromScroll(ItemStack scroll) {
        String spellData = getSpellData(scroll);
        if (spellData != null) {
            return Optional.ofNullable(SpellRegistry.getSpell(spellData));
        }
        return Optional.empty();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLevel.isClientSide) return;

        int timeCharged = this.getUseDuration(pStack) - pTimeCharged;

        if (timeCharged >= getUseDuration(pStack) - 1) {
            if (!pLevel.isClientSide) {
                if (pLivingEntity instanceof Player player) {
                    player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
                        getSpellClassFromScroll(pStack).ifPresent(spell -> {
                            if (!playerSpells.hasSpell(spell.getSpellId())) {
                                playerSpells.addSpell(spell.getSpellId());
                                ModMessages.sendToClient(new SpellsDataSyncS2CPacket(playerSpells.getSpells(), playerSpells.getActiveSpells(), playerSpells.getActiveSpellSlot()), (ServerPlayer) player);


                                player.sendSystemMessage(
                                        Component.translatable("text.wizardry.spells.learned_message", spell.getDisplayName()));
                                if (!player.isCreative() && !player.isSpectator()) pStack.shrink(1);
                            } else {
                                player.sendSystemMessage(
                                        Component.translatable("text.wizardry.spells.already_learned_message", spell.getDisplayName()).withStyle(ChatFormatting.RED));
                            }
                        });
                    });
                }
            }
        }

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    public static ItemStack createSpellScroll(AbstractSpell spell) {
        String spellId = spell.getSpellId();

        ItemStack stack = new ItemStack(ModItems.KNOWLEDGE_SCROLL.get());
        setSpellData(stack, spellId);

        return stack;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        int timeCharged = this.getUseDuration(pStack) - pRemainingUseDuration;


        if (timeCharged >= getUseDuration(pStack) - 1) {
            releaseUsing(pStack, pLevel, pLivingEntity, pRemainingUseDuration);
        }

        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        getSpellClassFromScroll(pStack).ifPresent(spell -> {
            pTooltipComponents.add(Component.translatable("text.wizardry.knowledge_scroll.info"));
            pTooltipComponents.add(Component.empty());

            List<MutableComponent> tooltipInfo = spell.getUniqueInfo();

            tooltipInfo.forEach(component -> {
                pTooltipComponents.add((Component) component);
            });
        });

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        AtomicReference<Component> displayName = new AtomicReference<>(null);

        getSpellClassFromScroll(pStack).ifPresent(spell -> {
            displayName.set(Component.translatable("text.wizardry.spells.display_name", spell.getDisplayName()));
        });

        return displayName.get() != null ? displayName.get() : super.getName(pStack);
    }


    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 100;
    }
}
