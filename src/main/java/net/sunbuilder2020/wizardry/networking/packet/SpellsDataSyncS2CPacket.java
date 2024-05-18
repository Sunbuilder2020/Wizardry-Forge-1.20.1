package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.client.ClientSpellsData;
import net.sunbuilder2020.wizardry.spells.AbstractSpell;
import net.sunbuilder2020.wizardry.spells.SpellRegistry;

import java.util.List;
import java.util.function.Supplier;

public class SpellsDataSyncS2CPacket {
    private final List<AbstractSpell> spells;
    private final List<AbstractSpell> activeSpells;
    private final int activeSpellSlot;

    public SpellsDataSyncS2CPacket(List<AbstractSpell> spells, List<AbstractSpell> activeSpells, int activeSpellSlot) {
        this.spells = spells;
        this.activeSpells = activeSpells;
        this.activeSpellSlot = activeSpellSlot;
    }

    public SpellsDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.spells = buf.readList(buffer -> SpellRegistry.getSpell(buffer.readUtf()));
        this.activeSpells = buf.readList(buffer -> SpellRegistry.getSpell(buffer.readUtf()));
        this.activeSpellSlot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(spells, (buffer, spell) -> buffer.writeUtf(spell.getSpellId()));
        buf.writeCollection(activeSpells, (buffer, spell) -> buffer.writeUtf(spell.getSpellId()));
        buf.writeInt(activeSpellSlot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientSpellsData.setSpells(spells);
            ClientSpellsData.setActiveSpells(activeSpells);
            ClientSpellsData.setActiveSpellSlot(activeSpellSlot);
        });
        return true;
    }
}