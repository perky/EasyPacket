package ljdp.easypacket.example;

import ljdp.easypacket.EasyPacketDispatcher;
import ljdp.easypacket.EasyPacketHandler;

public class Example {
	
	public Example() {
		EasyPacketDispatcher dispatcher = new EasyPacketDispatcher("examplechannel");
		ExamplePacket packet1 = new ExamplePacket("Luke Perkin", 21, true);
		EasyPacketHandler examplePacketHandler = EasyPacketHandler.registerEasyPacket(ExamplePacket.class, dispatcher);
		byte[] data = examplePacketHandler.write(packet1);
		
		ExamplePacket packet2 = new ExamplePacket();
		examplePacketHandler.read(packet2, data);
		
		assert packet1.age == packet2.age;
		assert packet1.name.equals(packet2.name);
		assert packet1.isMale == packet2.isMale;
		assert packet1.someLong == packet2.someLong;
	}
}
