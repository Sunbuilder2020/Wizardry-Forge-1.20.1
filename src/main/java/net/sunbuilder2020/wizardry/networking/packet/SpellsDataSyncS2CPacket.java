package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.client.ClientSpellsData;

import java.util.List;
import java.util.function.Supplier;

public class SpellsDataSyncS2CPacket {
    private final List<String> spells;
    private final List<String> activeSpells;
    private final int activeSpellSlot;

    public SpellsDataSyncS2CPacket(List<String> spells, List<String> activeSpells, int activeSpellSlot) {
        this.spells = spells;
        this.activeSpells = activeSpells;
        this.activeSpellSlot = activeSpellSlot;
    }

    public SpellsDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spells = buf.readList(FriendlyByteBuf::readUtf);
        this.activeSpells = buf.readList(FriendlyByteBuf::readUtf);
        this.activeSpellSlot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(spells, FriendlyByteBuf::writeUtf);
        buf.writeCollection(activeSpells, FriendlyByteBuf::writeUtf);
        buf.writeInt(activeSpellSlot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellsData.setSpellIDs(spells);
            ClientSpellsData.setActiveSpellIDs(activeSpells);
            ClientSpellsData.setActiveSpellSlot(activeSpellSlot);
        });
        return true;
    }
}
