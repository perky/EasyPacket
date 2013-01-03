package ljdp.easypacket;

public abstract class EasyPacket {
	
	@EasyPacketData
	public int id;

	public abstract boolean isChunkDataPacket();
	public abstract void onReceive();
	
}
