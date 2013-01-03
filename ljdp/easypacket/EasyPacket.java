package ljdp.easypacket;

import cpw.mods.fml.common.network.Player;

public abstract class EasyPacket {
	
	@EasyPacketData
	public int id;

	public abstract boolean isChunkDataPacket();
	public abstract void onReceive(Player player);
	
}
