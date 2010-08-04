package org.hfoss.adhoc;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AdhocFind {
	private static final String TAG = "AdhocFind";
	private HashMap<String, Object> find = null;

	public AdhocFind(HashMap<String, Object> findsMap) {
		find = findsMap; // just hope for the best for now
	}

	public AdhocFind(String json) {
		try {
			JSONObject jo = new JSONObject(json);
			find = new HashMap<String, Object>();
			Iterator keys = jo.keys();
			while(keys.hasNext()){
				String key = (String)keys.next();
				find.put(key, jo.get(key));
				Log.i(TAG, find.toString());
			}
			
		} catch (JSONException e) {
			Log.i(TAG, "not a JSON Object");
		}
		
	}

	@Override
	public String toString() {
		if (find == null)
			return null;
		JSONObject jo = new JSONObject(find);

		return jo.toString();
	}

	public void saveToDB() {
		Log.e(TAG, "saveToDB "+find);
		
	}

}
