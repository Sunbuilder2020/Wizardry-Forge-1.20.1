package net.sunbuilder2020.wizardry.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sunbuilder2020.wizardry.Wizardry;
import net.sunbuilder2020.wizardry.networking.packet.CastingDataSyncS2CPacket;
import net.sunbuilder2020.wizardry.networking.packet.SetSpellsDataC2SPacket;
import net.sunbuilder2020.wizardry.networking.packet.SpellCooldownsSyncS2CPacket;
import net.sunbuilder2020.wizardry.networking.packet.SpellsDataSyncS2CPacket;

public class ModMessages {
    private static SimpleChannel Instance;
    private static int packetID = 0;
    private static int ID() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Wizardry.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        Instance = net;

        net.messageBuilder(SpellsDataSyncS2CPacket.class, ID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(buf -> new SpellsDataSyncS2CPacket(buf))
                .encoder(SpellsDataSyncS2CPacket::toBytes)
                .consumerMainThread(SpellsDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(SetSpellsDataC2SPacket.class, ID(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(buf -> new SetSpellsDataC2SPacket(buf))
                .encoder(SetSpellsDataC2SPacket::toBytes)
                .consumerMainThread(SetSpellsDataC2SPacket::handle)
                .add();

        net.messageBuilder(CastingDataSyncS2CPacket.class, ID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(buf -> new CastingDataSyncS2CPacket(buf))
                .encoder(CastingDataSyncS2CPacket::toBytes)
                .consumerMainThread(CastingDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(SpellCooldownsSyncS2CPacket.class, ID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(buf -> new SpellCooldownsSyncS2CPacket(buf))
                .encoder(SpellCooldownsSyncS2CPacket::toBytes)
                .consumerMainThread(SpellCooldownsSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        Instance.sendToServer(message);
    }

    public static <MSG> void sendToServer(MSG message, ServerPlayer player) {
        Instance.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        Instance.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
