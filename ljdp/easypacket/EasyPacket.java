package ljdp.easypacket;

import cpw.mods.fml.common.network.Player;

public abstract class EasyPacket {
	
	@EasyPacketData
	public int id;
	
	/**
	 * Return whether or not this is a chunk data packet.
	 * @return boolean
	 */
	public abstract boolean isChunkDataPacket();
	
	/**
	 * Callback method when a packet of this type is received.
	 * @param player
	 */
	public abstract void onReceive(Player player);
	
}
