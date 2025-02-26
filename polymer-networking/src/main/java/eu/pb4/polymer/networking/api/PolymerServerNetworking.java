package eu.pb4.polymer.networking.api;


import eu.pb4.polymer.common.api.events.SimpleEvent;
import eu.pb4.polymer.networking.impl.ClientPackets;
import eu.pb4.polymer.networking.impl.NetworkHandlerExtension;
import eu.pb4.polymer.networking.impl.ServerPacketRegistry;
import eu.pb4.polymer.networking.impl.ServerPackets;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public final class PolymerServerNetworking {
    private PolymerServerNetworking() {
    }

    public static final SimpleEvent<BiConsumer<ServerPlayNetworkHandler, PolymerHandshakeHandler>> AFTER_HANDSHAKE_APPLY = new SimpleEvent<>();
    /**
     * Creates versioned PacketByteBuf
     *
     * @param version Packet version
     * @return PacketByteBuf
     */
    public static PacketByteBuf buf(int version) {
        var buf = new PacketByteBuf(Unpooled.buffer());
        return buf.writeVarInt(version);
    }

    public static boolean send(ServerPlayNetworkHandler handler, Identifier identifier, ServerPacketWriter packet) {
        handler.sendPacket(packet.toPacket(identifier));
        return true;
    }

    public static boolean sendDirect(ServerPlayNetworkHandler handler, Identifier identifier, PacketByteBuf packetByteBuf) {
        handler.sendPacket(new CustomPayloadS2CPacket(identifier, packetByteBuf));
        return true;
    }

    public static boolean registerPacketHandler(Identifier identifier, PolymerServerPacketHandler handler, int... supportedVersions) {
        if (!ServerPacketRegistry.PACKETS.containsKey(identifier)) {
            ClientPackets.register(identifier, supportedVersions);
            ServerPacketRegistry.PACKETS.put(identifier, handler);
            return true;
        }
        return false;
    }

    public static boolean registerSendPacket(Identifier identifier, int... supportedVersions) {
        ServerPackets.register(identifier, supportedVersions);
        return true;
    }

    public static int getSupportedVersion(ServerPlayNetworkHandler handler, Identifier serverPacket) {
        return ((NetworkHandlerExtension) handler).polymerNet$getSupportedVersion(serverPacket);
    }

    public static long getLastPacketReceivedTime(ServerPlayNetworkHandler handler, Identifier identifier) {
        return ((NetworkHandlerExtension) handler).polymerNet$lastPacketUpdate(identifier);
    }

    static {
        ClientPackets.register();
        ServerPackets.register();
    }
}
