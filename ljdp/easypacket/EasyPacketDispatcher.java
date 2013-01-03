package ljdp.easypacket;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class EasyPacketDispatcher implements IPacketHandler {
	
	private String channel;
	
	public EasyPacketDispatcher(String channel) {
		this.channel = channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public Packet250CustomPayload createCustomPacket(byte[] data, boolean isChunkData) {
		Packet250CustomPayload customPacket = new Packet250CustomPayload();
		customPacket.channel = this.channel;
		customPacket.data = data;
		customPacket.isChunkDataPacket = isChunkData;
		return customPacket;
	}
	
	public void sendDataToServer(byte[] data, boolean isChunkData) {
		Packet250CustomPayload packet = createCustomPacket(data, isChunkData);
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public void sendDataToPlayer(byte[] data, boolean isChunkData, Player player) {
		Packet250CustomPayload packet = createCustomPacket(data, isChunkData);
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}
	
	public void sendDataToAllPlayers(byte[] data, boolean isChunkData) {
		Packet250CustomPayload packet = createCustomPacket(data, isChunkData);
		PacketDispatcher.sendPacketToAllPlayers(packet);
	}
	
	public void sendDataToAllPlayersInDimension(byte[] data, boolean isChunkData, int dimensionID) {
		Packet250CustomPayload packet = createCustomPacket(data, isChunkData);
		PacketDispatcher.sendPacketToAllInDimension(packet, dimensionID);
		
	}
	
	public void sendDataToAllPlayersAround(byte[] data, boolean isChunkData, 
			double x, double y, double z, double range, int dimensionID)
	{
		Packet250CustomPayload packet = createCustomPacket(data, isChunkData);
		PacketDispatcher.sendPacketToAllAround(x, y, z, range, dimensionID, packet);
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		EasyPacketHandler.onPacketReceived(manager, packet, player);
	}
	
	
}
