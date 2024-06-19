package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.client.ClientCastingData;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.function.Supplier;

public class CastingDataSyncS2CPacket {
    private final AbstractSpell currentSpell;
    private final int castTimeRemaining;

    public CastingDataSyncS2CPacket(AbstractSpell currentSpell, int castTimeRemaining) {
        this.currentSpell = currentSpell;
        this.castTimeRemaining = castTimeRemaining;
    }

    public CastingDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.currentSpell = SpellRegistry.getSpell(buf.readUtf());
        this.castTimeRemaining = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(currentSpell != null ? currentSpell.getSpellId() : "");
        buf.writeInt(castTimeRemaining);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientCastingData.setCurrentSpell(currentSpell);
            ClientCastingData.setCastTimeRemaining(castTimeRemaining);
        });
        return true;
    }
}
