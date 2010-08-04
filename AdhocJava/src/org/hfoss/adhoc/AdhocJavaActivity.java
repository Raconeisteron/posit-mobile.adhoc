package org.hfoss.adhoc;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdhocJavaActivity extends Activity implements OnClickListener {
	private static final String TAG = "AdhocJavaActivity";
	/** The primary interface we will be calling on the service. */
    IAdhocService mService = null;
    /** Another interface we use on the service. */
    IAdhocService mSecondaryService = null;
    private boolean mIsBound;

	/** Called when the activity is first created. */
	// Adhoc adhoc = Adhoc.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button b = (Button) findViewById(R.id.adhocButton);
		b.setOnClickListener(this);

		Button b2 = (Button) findViewById(R.id.bcastButton);
		b2.setOnClickListener(this);
		HashMap<String, Object> findsMap = new HashMap<String, Object>();
		findsMap.put("test", "value ");
		AdhocFind adhocFind= new AdhocFind(findsMap);
		AdhocData<AdhocFind> adhocData = new AdhocData<AdhocFind>(this,adhocFind);
//		String s = adhocData.toString();
//		Log.i(TAG, s);
//		Log.i(TAG, adhocFind+" " + adhocFind.toString().getBytes().length );
//		Log.i(TAG,s.getBytes().length+"");
//		Log.i(TAG, s.substring(23, s.length()));
		
		getMACAddress();

	}
	
	private String getMACAddress(){
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()){
			return null;
		}
		// Get WiFi status
		WifiInfo info = wifi.getConnectionInfo();
		MacAddress mc = new MacAddress(info.getMacAddress());
		Log.i(TAG, mc+ "  "+ mc.toByteString());
		return "boo";
	}
	

	private void startAdhocService() {
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(this, AdhocService.class);
		startService(serviceIntent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.adhocButton:
			// adhoc.listen();
			Log.i(TAG, "adhoc button");
			startAdhocService();
			startQueueService();
			break;
		case R.id.bcastButton:
			HashMap<String, Object> findsMap = new HashMap<String, Object>();
			findsMap.put("test", "value"+ System.currentTimeMillis());
			AdhocFind adhocFind= new AdhocFind(findsMap);
			AdhocData<AdhocFind> adhocData = new AdhocData<AdhocFind>(this,adhocFind);
			
			Queues.outputQueue.add(adhocData);

//			Log.i(TAG, Queues.outputQueue.size() + "");
			break;
		default:
			break;
		}

	}

	private void startQueueService() {
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(this, QueueService.class);
		startService(serviceIntent);
		
	}
	
	
//	 private ServiceConnection mConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			  mService = IAdhocService.Stub.asInterface(service);
//			  try {
//	                mService.registerCallback(mCallback);
//	            } catch (RemoteException e) {
//	                // In this case the service has crashed before we could even
//	                // do anything with it; we can count on soon being
//	                // disconnected (and then reconnected if it can be restarted)
//	                // so there is no need to do anything here.
//	            }
//
//			
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			// TODO Auto-generated method stub
//			
//		}
//	 };
//	 
//	 
//	   /**
//	     * This implementation is used to receive callbacks from the remote
//	     * service.
//	     */
//	    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
//	        /**
//	         * This is called by the remote service regularly to tell us about
//	         * new values.  Note that IPC calls are dispatched through a thread
//	         * pool running in each process, so the code executing here will
//	         * NOT be running in our main thread like most other things -- so,
//	         * to update the UI, we need to use a Handler to hop over there.
//	         */
//	        public void valueChanged(int value) {
//	            mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, value, 0));
//	        }
//	    };

}