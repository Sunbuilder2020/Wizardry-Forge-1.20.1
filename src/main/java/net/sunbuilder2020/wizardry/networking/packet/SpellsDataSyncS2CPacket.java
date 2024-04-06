package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.client.ClientSpellsData;

import java.util.List;
import java.util.function.Supplier;

public class SpellsDataSyncS2CPacket {
    private final List<String> spells;

    public SpellsDataSyncS2CPacket(List<String> spells) {
        this.spells = spells;
    }

    public SpellsDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spells = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(spells, FriendlyByteBuf::writeUtf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellsData.set(spells);
        });
        return true;
    }
}

