package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.spells.PlayerSpellsProvider;

import java.util.function.Supplier;

public class AddSpellC2SPacket {
    private final String spell;

    public AddSpellC2SPacket(String spell) {
        this.spell = spell;
    }

    public AddSpellC2SPacket(FriendlyByteBuf buf) {
        this.spell = buf.readUtf(32767);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.spell);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spells -> {
                    spells.addSpell(spell);

                    ModMessages.sendToClient(new SpellsDataSyncS2CPacket(spells.getSpells()), player);
                });
            }
        });
        return true;
    }
}

