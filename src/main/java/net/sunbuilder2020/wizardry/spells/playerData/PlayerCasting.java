package net.sunbuilder2020.wizardry.spells.playerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.sunbuilder2020.wizardry.networking.ModMessages;
import net.sunbuilder2020.wizardry.networking.packet.CastingDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;
import net.sunbuilder2020.wizardry.spells.spells.NoneSpell;

import java.util.ArrayList;

@AutoRegisterCapability
public class PlayerCasting {
    private AbstractSpell currentSpell;
    private int castTimeRemaining;

    public AbstractSpell getCurrentSpell() {
        return currentSpell;
    }

    public void startCasting(AbstractSpell spell) {
        this.currentSpell = spell;
        this.castTimeRemaining = spell.castTime();
    }

    public void stopCasting() {
        this.currentSpell = SpellRegistry.noneSpell();
        this.castTimeRemaining = 0;
    }

    public void tick(ServerPlayer player) {
        if (currentSpell != null && !(currentSpell instanceof NoneSpell)) {
            if (castTimeRemaining > 0) {
                castTimeRemaining--;

                if (castTimeRemaining <= 0) {
                    currentSpell.onCast(player);

                    stopCasting();
                    syncData(player);
                }
            }
        }
    }

    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public void deserializeNBT(CompoundTag nbt) {
    }

    public boolean isCasting() {
        return currentSpell != null && !(currentSpell instanceof NoneSpell) && castTimeRemaining > 0;
    }

    public int getCastTimeRemaining() {
        return castTimeRemaining;
    }

    public void copyFrom(PlayerCasting source) {
        this.currentSpell = source.getCurrentSpell();
        this.castTimeRemaining = source.getCastTimeRemaining();
    }

    public void syncData(ServerPlayer player) {
        ModMessages.sendToClient(new CastingDataSyncS2CPacket(currentSpell, castTimeRemaining), player);
    }
}
