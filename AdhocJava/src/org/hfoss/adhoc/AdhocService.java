package org.hfoss.adhoc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class AdhocService extends Service {
	protected static final String TAG = "AdhocService";
	int mPort = 4959;
	Adhoc adhoc = Adhoc.getInstance(this);
	boolean stopped = false;
	private final IAdhocService.Stub mBinder = new IAdhocService.Stub() {

		public int getPort() {
			return mPort;
		}
	};
	@Override
	public void onCreate() {
		super.onCreate();
		Thread listenerThread = new Thread(){
			@Override
			public void run() {
				Looper.prepare();
					
					adhoc.listen();
					Log.i(TAG, "started listening");
				
				
			}
		};
		listenerThread.start();
		
		
		Thread senderThread = new Thread(){
			public void run() {
				Looper.prepare();
				adhoc.sendData();
				Log.i(TAG, "started sending");
			};
		};
		senderThread.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		adhoc.stopListening();
	}
	@Override
	public IBinder onBind(Intent intent) {
		if (IAdhocService.class.getName().equals(intent.getAction())) {
			return mBinder;
		}
		return null;
	}

}
