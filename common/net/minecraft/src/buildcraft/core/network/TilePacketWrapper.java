/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.core.network;

import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.core.ByteBuffer;
import net.minecraft.src.buildcraft.core.ClassMapping;
import net.minecraft.src.buildcraft.core.ClassMapping.Indexes;

public class TilePacketWrapper {

	ClassMapping rootMappings [];

	@SuppressWarnings("rawtypes")
	public TilePacketWrapper (Class c) {
		this (new Class [] {c});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TilePacketWrapper (Class c []) {
		rootMappings = new ClassMapping [c.length];

		for (int i = 0; i < c.length; ++i)
			rootMappings [i] = new ClassMapping(c [i]);
	}


	public PacketPayload toPayload(TileEntity tile) {
		int sizeF = 0, sizeS = 0;

		for (int i = 0; i < rootMappings.length; ++i) {
			int [] size = rootMappings [i].getSize();

			sizeF += size [1];
			sizeS += size [2];
		}

		PacketPayload payload = new PacketPayload(0, sizeF, sizeS);

		ByteBuffer buf = new ByteBuffer();

		buf.writeInt(tile.xCoord);
		buf.writeInt(tile.yCoord);
		buf.writeInt(tile.zCoord);

		try {
			rootMappings [0].setData(tile, buf, payload.floatPayload,
					payload.stringPayload, new Indexes(0, 0));

			payload.intPayload = buf.readIntArray();

			return payload;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public PacketPayload toPayload(int x, int y, int z, Object obj) {
		return toPayload(x, y, z, new Object [] {obj});
	}

	public PacketPayload toPayload(int x, int y, int z, Object [] obj) {

		int sizeF = 0, sizeS = 0;

		for (int i = 0; i < rootMappings.length; ++i) {
			int [] size = rootMappings [i].getSize();

			sizeF += size [1];
			sizeS += size [2];
		}

		PacketPayload payload = new PacketPayload(0, sizeF, sizeS);

		ByteBuffer buf = new ByteBuffer();

		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

		try {
			Indexes ind = new Indexes(0, 0);

			for (int i = 0; i < rootMappings.length; ++i)
				rootMappings [i].setData(obj [i], buf, payload.floatPayload,
						payload.stringPayload, ind);

			payload.intPayload = buf.readIntArray();

			return payload;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}


	public void fromPayload(TileEntity tile, PacketPayload packet) {
		try {
			ByteBuffer buf = new ByteBuffer();
			buf.writeIntArray(packet.intPayload);
			buf.readInt();
			buf.readInt();
			buf.readInt();

			rootMappings [0].updateFromData(tile, buf, packet.floatPayload,
					packet.stringPayload, new Indexes(0, 0));

			packet.intPayload = buf.readIntArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fromPayload(Object obj, PacketPayload packet) {
		fromPayload(new Object [] {obj}, packet);
	}

	public void fromPayload(Object [] obj, PacketPayload packet) {
		try {
			ByteBuffer buf = new ByteBuffer();
			buf.writeIntArray(packet.intPayload);
			buf.readInt();
			buf.readInt();
			buf.readInt();

			Indexes ind = new Indexes(0, 0);

			for (int i = 0; i < rootMappings.length; ++i)
				rootMappings [i].updateFromData(obj [i], buf, packet.floatPayload,
						packet.stringPayload, ind);

			packet.intPayload = buf.readIntArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	public Packet230ModLoader toPacket (TileEntity tile) {
		Packet230ModLoader packet = new Packet230ModLoader();
		packet.modId = mod_BuildCraftCore.instance.getId();
		packet.isChunkDataPacket = true;
		packet.packetType = packetType.ordinal();

		int sizeF = 0, sizeS = 0;

		for (int i = 0; i < rootMappings.length; ++i) {
			int [] size = rootMappings [i].getSize();

			sizeF += size [1];
			sizeS += size [2];
		}

		packet.dataFloat = new float [sizeF];
		packet.dataString = new String [sizeS];

		ByteBuffer buf = new ByteBuffer();

		buf.writeInt(tile.xCoord);
		buf.writeInt(tile.yCoord);
		buf.writeInt(tile.zCoord);

		try {
			rootMappings [0].setData(tile, buf, packet.dataFloat,
					packet.dataString, new Indexes(0, 0));

			packet.dataInt = buf.readIntArray();

			return packet;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public Packet230ModLoader toPacket (int x, int y, int z, Object obj) {
		return toPacket(x, y, z, new Object [] {obj});
	}

	public Packet230ModLoader toPacket (int x, int y, int z, Object [] obj) {
		Packet230ModLoader packet = new Packet230ModLoader();
		packet.modId = mod_BuildCraftCore.instance.getId();
		packet.isChunkDataPacket = true;
		packet.packetType = packetType.ordinal();

		int sizeF = 0, sizeS = 0;

		for (int i = 0; i < rootMappings.length; ++i) {
			int [] size = rootMappings [i].getSize();

			sizeF += size [1];
			sizeS += size [2];
		}

		packet.dataFloat = new float [sizeF];
		packet.dataString = new String [sizeS];

		ByteBuffer buf = new ByteBuffer();

		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

		try {
			Indexes ind = new Indexes(0, 0);

			for (int i = 0; i < rootMappings.length; ++i) {
				rootMappings [i].setData(obj [i], buf, packet.dataFloat,
						packet.dataString, ind);
			}

			packet.dataInt = buf.readIntArray();

			return packet;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}*/


	/*
	public void updateFromPacket (Object obj, Packet230ModLoader packet) {
		updateFromPacket(new Object [] {obj}, packet);
	}

	public void updateFromPacket (Object [] obj, Packet230ModLoader packet) {
		try {
			ByteBuffer buf = new ByteBuffer();
			buf.writeIntArray(packet.dataInt);
			buf.readInt();
			buf.readInt();
			buf.readInt();

			Indexes ind = new Indexes(0, 0);

			for (int i = 0; i < rootMappings.length; ++i)
				rootMappings [i].updateFromData(obj [i], buf, packet.dataFloat,
						packet.dataString, ind);

			packet.dataInt = buf.readIntArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateFromPacket (TileEntity tile, Packet230ModLoader packet) {
		try {
			ByteBuffer buf = new ByteBuffer();
			buf.writeIntArray(packet.dataInt);
			buf.readInt();
			buf.readInt();
			buf.readInt();

			rootMappings [0].updateFromData(tile, buf, packet.dataFloat,
					packet.dataString, new Indexes(0, 0));

			packet.dataInt = buf.readIntArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}