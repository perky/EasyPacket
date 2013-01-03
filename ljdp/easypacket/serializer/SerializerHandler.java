package ljdp.easypacket.serializer;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

public class SerializerHandler {
	
	public static final SerializerHandler instance = new SerializerHandler();
	
	public HashMap<Class<?>, Serializer> serializers = new HashMap();
	private SerializerHandler() {
		serializers.put(String.class, new StringSerializer());
		serializers.put(Long.class, new LongSerializer());
		serializers.put(Short.class, new ShortSerializer());
		serializers.put(Integer.class, new IntegerSerializer());
		serializers.put(boolean.class, new BooleanSerializer());
		serializers.put(byte[].class, new ByteArraySerializer());
		serializers.put(ItemStack.class, new ItemStackSerializer());
	}
	
	public void registerSerializer(Class<?> clazz, Serializer serializer) {
		serializers.put(clazz, serializer);
	}
	
	public Serializer getSerializer(Class<?> clazz) {
		return serializers.get(clazz);
	}
	
}
