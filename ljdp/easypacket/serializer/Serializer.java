package ljdp.easypacket.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class Serializer {
	
	public static HashMap<Class<?>, Serializer> serializers = new HashMap();
	
	public abstract void write(Object obj, Field field, DataOutputStream out) throws IOException, IllegalArgumentException, IllegalAccessException;
	public abstract void read(Object obj, Field field, DataInputStream in) throws IOException, IllegalArgumentException, IllegalAccessException;
}
