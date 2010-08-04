package org.hfoss.adhoc;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class AdhocUtils {
	public static MacAddress getMacAddress(Context cxt){
		WifiManager wifi = (WifiManager) cxt.getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()){
			return null;
		}
		WifiInfo info = wifi.getConnectionInfo();
		return new MacAddress(info.getMacAddress());
		
	}
	
	public static byte[] shortToBytes(short num){
		// take 16-bit short apart into two 8-bit bytes.
		byte[] b = new byte[2];
		b[0] = (byte) (num >>> 8); //high order byte
		b[1] = (byte) num; /* cast implies & 0xff */
		return b;
	}
}
