package net.sunbuilder2020.wizardry.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.sunbuilder2020.wizardry.client.ClientSpellsData;
import net.sunbuilder2020.wizardry.spells.playerData.PlayerSpellsProvider;

import java.util.List;
import java.util.function.Supplier;

public class SetSpellsDataC2SPacket {
    private final List<String> spells;
    private final List<String> activeSpells;
    private final int activeSpellSlot;

    public SetSpellsDataC2SPacket(List<String> spells, List<String> activeSpells, int activeSpellSlot) {
        this.spells = spells;
        this.activeSpells = activeSpells;
        this.activeSpellSlot = activeSpellSlot;
    }

    public SetSpellsDataC2SPacket(FriendlyByteBuf buf) {
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
        ServerPlayer player = supplier.get().getSender();

        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(playerSpells -> {
                playerSpells.setSpells(spells);
                playerSpells.setActiveSpells(activeSpells);
                playerSpells.setActiveSpellSlot(activeSpellSlot);
            });
        });
        return true;
    }
}
