package org.hfoss.adhoc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;

public class AdhocData<T> implements Serializable {
	static private final long serialVersionUID = 1L;

	private static final String TAG = "AdhocData";
	private short packetLength=0;
	private byte messageType=0;
	private byte hops=0;
	private short TTL=0;
	private short groupSize=0;
	private short sequenceNumber=0;
	private MacAddress origin = new MacAddress();
	private MacAddress target = new MacAddress();
	private MacAddress sender = new MacAddress();
	private short[] visited = new short[16];
	private short[] recentVisited = new short[16];
	private T message;

	public short getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(short packetLength) {
		this.packetLength = packetLength;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}

	public byte getHops() {
		return hops;
	}

	public void setHops(byte hops) {
		this.hops = hops;
	}

	public short getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(short groupSize) {
		this.groupSize = groupSize;
	}

	public short[] getVisited() {
		return visited;
	}

	public void setVisited(short[] visited) {
		this.visited = visited;
	}

	public short[] getRecentVisited() {
		return recentVisited;
	}

	public void setRecentVisited(short[] recentVisited) {
		this.recentVisited = recentVisited;
	}

	public MacAddress getOrigin() {
		return origin;
	}

	public void setOrigin(MacAddress origin) {
		this.origin = origin;
	}

	public MacAddress getTarget() {
		return target;
	}

	public void setTarget(UnsignedByte[] targetBytes) {
		target = new MacAddress(targetBytes);
	}

	public void setTarget(MacAddress target) {
		this.target = target;
	}

	public MacAddress getSender() {
		return sender;
	}

	public void setSender(MacAddress sender) {
		this.sender = sender;
	}

	public AdhocData(T msg) {
		messageType = 0;
		hops = 0;
		TTL = 0;
		groupSize = 0;
		sequenceNumber = 0;
		message = msg;

	}

	public AdhocData(Context cxt, T msg) {
		this(msg);
		origin = AdhocUtils.getMacAddress(cxt);
		sender = AdhocUtils.getMacAddress(cxt);
	}

	public AdhocData(byte[] bytes) {
		try {
			byte[] c = new byte[6];

			int idx = 13;
			for (int i = idx; i < idx + 6; i++) {

				// c[i - idx] = new UnsignedByte(pBytes[i]);
				c[i - idx] = bytes[i];
			}

			MacAddress mc = new MacAddress(c);
			Log.i(TAG, mc + "");
			Log.i(TAG, "here");
			// Log.i(TAG, mc.toByteString()+"");
//			int dataSize = Integer.parseInt(p.substring(0, 2));
//			Log.i(TAG, dataSize + "");
//			Log.i(TAG, p.substring(2, 2 + dataSize));
			// this needs to be better expressed. HACK
//			message = (T) new AdhocFind(p.substring(2, 2 + dataSize));
		} catch (Exception e) {
			Log.e(TAG, "Malformatted data or offset error");

		}
	}

	/*
	 * This is for initializing from the received packet string
	 */
	public AdhocData(String packet) {
		String p = packet.substring(Adhoc.HEADER_SIZE);

		Log.i(TAG, p);
		try {
			byte[] pBytes = packet.getBytes();

			// UnsignedByte[] c = new UnsignedByte[6];
			byte[] c = new byte[6];

			int idx = 10;
			for (int i = idx; i < idx + 6; i++) {

				// c[i - idx] = new UnsignedByte(pBytes[i]);
				c[i - idx] = pBytes[i];
			}

			MacAddress mc = new MacAddress(c);
			Log.i(TAG, mc + "");
			// Log.i(TAG, mc.toByteString()+"");
			int dataSize = Integer.parseInt(p.substring(0, 2));
			Log.i(TAG, dataSize + "");
			Log.i(TAG, p.substring(2, 2 + dataSize));
			// this needs to be better expressed. HACK
			message = (T) new AdhocFind(p.substring(2, 2 + dataSize));
		} catch (Exception e) {
			Log.e(TAG, "Malformatted data or offset error");

		}

	}

	public short getTTL() {
		return TTL;
	}

	public void setTTL(short tTL) {
		TTL = tTL;
	}

	public short getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(short sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}

	@Override
	public String toString() {
		String result = "null";
		try {
			result = String.format("%d %d %d %d %d %s %s %s %s %s",
					messageType, hops, TTL, groupSize, sequenceNumber, origin
							.toString(), target.toString(), sender.toString(),
					message.toString().getBytes().length, message.toString());
		} catch (NullPointerException ne) {
			Log.e(TAG,
					"NullPointerException when creating string for AdhocData "
							+ ne.getMessage());
		}
		return result;
	}

	public byte[] visitedNodesToBytes(short[] visited) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (short v : visited) {
			baos.write(AdhocUtils.shortToBytes(v));
		}
		return baos.toByteArray();
	}

	public byte[] toBytes() {
		ByteArrayOutputStream baos0 = new ByteArrayOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			baos0.write(messageType);
			baos0.write(hops);
			baos0.write(AdhocUtils.shortToBytes(TTL));
			baos0.write(AdhocUtils.shortToBytes(groupSize));
			baos0.write(AdhocUtils.shortToBytes(sequenceNumber));
			baos0.write(origin.tosignedByteArray());
			baos0.write(target.tosignedByteArray());
			baos0.write(sender.tosignedByteArray());
			baos0.write(visitedNodesToBytes(visited));
			baos0.write(visitedNodesToBytes(recentVisited));
			short messageLength = (short) (message.toString().getBytes().length);
			baos0.write(AdhocUtils.shortToBytes(messageLength));
			baos0.write(message.toString().getBytes());
			// find the packet length and write it to the byteArray
			packetLength = (short) baos0.toByteArray().length;
			baos.write(AdhocUtils.shortToBytes(packetLength));
			// write the bytearray with all the data
			baos.write(baos0.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "cannot write");
		}
		return baos.toByteArray();
	}

}
