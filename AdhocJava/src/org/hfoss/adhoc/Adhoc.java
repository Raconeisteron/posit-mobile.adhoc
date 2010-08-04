package org.hfoss.adhoc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import android.content.Context;
import android.util.Log;

public class Adhoc {
	private static final String TAG = "Adhoc";
	private static Adhoc instance = null;
	public static final String PROTOCOL_ID = "RWG";
	public static final int HEADER_SIZE = 23; // This is subject to change but the concepts will remain the same

	public static Adhoc getInstance(Context cxt) {
		if (instance == null) {
			instance = new Adhoc(cxt);
		}
		return instance;
	}

	private int mPort = 4959;
	private int maxLength = 1024;
	private DatagramSocket listeningSocket = null;
	private DatagramSocket bcastSocket = null;
	private boolean stopped = false;
	private boolean sending= false;
	private Context mContext;

	private Adhoc(Context cxt) {
		mContext = cxt;
		try {
			Log.i(TAG, "here");
			listeningSocket = new DatagramSocket(mPort);
			listeningSocket.setReuseAddress(true);

		} catch (SocketException e) {
			Log.e(TAG, "failed to create listening socket " + e.getMessage());
		}

		try {
			bcastSocket = new DatagramSocket();
		} catch (SocketException e) {
			Log.e(TAG, "failed to create bcast socket " + e.getMessage());

		}
		
	}

	public void listen()  {
		if (listeningSocket == null)
			return;
		try {
			InetAddress IPAddress = InetAddress.getByName("");
		} catch (UnknownHostException e1) {
			Log.e(TAG, "cannot get address");
		}
		while (!stopped) {
			byte[] buf = new byte[maxLength];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			
			try {
//				
//
				listeningSocket.receive(packet);
//
//				
				String s = new String(packet.getData());
//				try {
				if (s.startsWith(PROTOCOL_ID)){
					AdhocData<AdhocFind> adhocData = new AdhocData<AdhocFind>(packet.getData());
				}
//					AdhocData<AdhocFind> adhocData = new AdhocData<AdhocFind>(s.substring(PROTOCOL_ID.length()));
//					Log.i(TAG, adhocData.getSender()+"");
//					//add to the input queue only if it's not sent by the same phone
//					if (! adhocData.getSender().equals(AdhocUtils.getMacAddress(mContext))){
//						Queues.inputQueue.put(adhocData);
//					}
////				
//				}
//				}catch(InterruptedException e){
//					Log.e(TAG, "Cannot add the adhoc data to inputqueue");
//				}
//				Log.i(TAG, "inputqueue size "+Queues.inputQueue.size());
			} catch (IOException e) {
				Log.e(TAG, "failed to receive IOException " + e.getMessage());
			}
		}

	}

	public void stopListening() {
		stopped = true;
	}

	public void sendData() {
		while (!stopped) {
			Log.i(TAG, "running "+ Queues.outputQueue.size());
				
				 try {
					 AdhocData data = Queues.outputQueue.take();
				Log.e(TAG,  data+ " here");
				sending = true;
				//broadcast(PROTOCOL_ID+data.toString());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				baos.write(PROTOCOL_ID.toString().getBytes());
				baos.write(data.toBytes());
				broadcast(baos.toByteArray());
//				broadcast(data.toBytes());
				sending = false;
				 } catch (InterruptedException e) {
				 Log.e(TAG, "interrupted");
				 } catch (IOException e) {
					 Log.e(TAG, "IOException! wasn't able to write to the byteArray");
				 }
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Thread interrupted");
			}

		}
	}

	private void broadcast (byte[] bytes){
		// System.arraycopy(data.toCharArray(), 0, bytes, 0, data.length());
		InetAddress IPAddress;
		try {
			IPAddress = InetAddress.getByName("255.255.255.255");
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
					IPAddress, mPort);

			bcastSocket.setBroadcast(true);
			bcastSocket.send(packet);
//			Log.i(TAG, "sent " + data);
			// bcastSocket.close();
		} catch (UnknownHostException e1) {
			Log.e(TAG, "cannot get address");
		} catch (SocketException e) {
			Log.e(TAG, "failed to create bcast socket");
		} catch (IOException e) {
			Log.e(TAG, "can't send");
		}
	}
	
	public void broadcast(String data) {
		if (bcastSocket == null)
			return;
		// byte[] bytes = new byte[data.length()];
		byte[] bytes = data.getBytes();
		broadcast(bytes);
	}

}
