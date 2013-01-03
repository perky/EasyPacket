package ljdp.easypacket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import ljdp.easypacket.serializer.Serializer;
import ljdp.easypacket.serializer.SerializerHandler;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.network.Player;

public class EasyPacketHandler {
	
	private static int nextPacketID = 0;
	private static HashMap<Integer, EasyPacketHandler> handlers = new HashMap();
	private static class DataField {
		Class<?> type;
		Field field;
	}
	
	/**
	 * Registers an EasyPacket class and returns an EasyPacketHandler
	 * @param clazz the EasyPacket class to register
	 * @param dispatcher The dispatcher to bind the handler to.
	 * @return
	 */
	public static EasyPacketHandler registerEasyPacket(Class<? extends EasyPacket> clazz, EasyPacketDispatcher dispatcher) {
		int packetID = nextPacketID++;
		EasyPacketHandler packetHandler = new EasyPacketHandler(packetID, dispatcher);
		handlers.put(packetID, packetHandler);
		Field[] fields = clazz.getFields();
		for(Field field : fields) {
			Annotation dataAnnotation = field.getAnnotation(EasyPacketData.class);
			if(dataAnnotation != null) {
				registerDataField(packetHandler, field, (EasyPacketData)dataAnnotation);
			}
		}
		return packetHandler;
	}

	private static void registerDataField(EasyPacketHandler handler, Field field, EasyPacketData dataAnnotation) {
		DataField dataField = new DataField();
		dataField.type = field.getType();
		dataField.field = field;
		handler.dataFields.add(dataField);
	}
	
	protected static void onPacketReceived(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		int packetID = getPacketIDFromData(packet.data);
		EasyPacketHandler packetHandler = handlers.get(packetID);
		packetHandler.onDataReceived(packet.data, player);
	}
	
	private static int getPacketIDFromData(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream(in);
		int readPacketID = -1;
		try {
			readPacketID = din.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readPacketID;
	}
	
	private int packetID;
	private List<DataField> dataFields = new ArrayList();
	private EasyPacketDispatcher dispatcher;
	private Class<? extends EasyPacket> packetClass;
	
	private EasyPacketHandler(int packetID, EasyPacketDispatcher dispatcher) {
		this.packetID = packetID;
		this.dispatcher = dispatcher;
	}
	
	/**
	 * Creates an empty packet.
	 * @return
	 */
	public EasyPacket createPacket() {
		EasyPacket easyPacket = null;
		try {
			easyPacket = packetClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return easyPacket;
	}
	
	private void onDataReceived(byte[] data, Player player) {
		EasyPacket easyPacket = createPacket();
		if(easyPacket != null) {
			read(easyPacket, data);
			easyPacket.onReceive(player);
		}
	}
	
	/**
	 * Writes the all fields annotated with @EasyPacketData to a byte array.
	 * @param easyPacket
	 * @return byte[] data
	 */
	public byte[] write(EasyPacket easyPacket) {
		easyPacket.id = packetID;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		for(DataField dataField : dataFields) {
			Serializer serializer = SerializerHandler.instance.getSerializer(dataField.type);
			if(serializer == null) continue;
			try {
				serializer.write(easyPacket, dataField.field, dos);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * Reads a byte array and inserts the values in the packet's fields annotated with @EasyPacketData
	 * @param easyPacket
	 * @param data
	 */
	public void read(EasyPacket easyPacket, byte[] data) {
		easyPacket.id = packetID;
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		for(DataField dataField : dataFields) {
			Serializer serializer = SerializerHandler.instance.getSerializer(dataField.type);
			if(serializer == null) continue;
			try {
				serializer.read(easyPacket, dataField.field, dis);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendToServer(EasyPacket packet) {
		byte[] data = write(packet);
		dispatcher.sendDataToServer(data, packet.isChunkDataPacket());
	}
	
	public void sendToAllPlayers(EasyPacket packet) {
		byte[] data = write(packet);
		dispatcher.sendDataToAllPlayers(data, packet.isChunkDataPacket());
	}
	
	public void sendToAllPlayersInDimension(EasyPacket packet, int dimensionID) {
		byte[] data = write(packet);
		dispatcher.sendDataToAllPlayersInDimension(data, packet.isChunkDataPacket(), dimensionID);
	}
	
	public void sendToAllPlayersAround(EasyPacket packet, double x, double y, double z, double range, int dimensionID) {
		byte[] data = write(packet);
		dispatcher.sendDataToAllPlayersAround(data, packet.isChunkDataPacket(), x, y, z, range, dimensionID);
	}
	
	public void sendToPlayer(EasyPacket packet, Player player) {
		byte[] data = write(packet);
		dispatcher.sendDataToPlayer(data, packet.isChunkDataPacket(), player);
	}
	
}
