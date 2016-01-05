package com.robdich.videoplayerdemo.video;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

class URLHistory extends ArrayAdapter<URLHistory.HistoryItem> {
	private ArrayList<URLHistory.HistoryItem> spareCopy=
		new ArrayList<URLHistory.HistoryItem>();
	
	URLHistory(Context context, int resource) {
		super(context, resource,
						new ArrayList<URLHistory.HistoryItem>());
	}
	
	void update(String url) {
		for (HistoryItem h : spareCopy) {
			if (url.equals(h.url)) {
				h.count++;
				return;
			}
		}
		
		HistoryItem h=new HistoryItem(url);
		
		spareCopy.add(h);
		add(h);						// duplicate due to moronic filtering
	}
	
	void save(Writer out) throws JSONException, IOException {
		JSONStringer json=new JSONStringer().object();
									
		for (HistoryItem h : spareCopy) {
			h.emit(json);
		}
		 
		out.write(json.endObject().toString());
	}
	
	void load(String rawJSON) throws JSONException {
		JSONObject json=new JSONObject(rawJSON);
									
		for (Iterator i=json.keys(); i.hasNext(); ) {
			String key=i.next().toString();
      HistoryItem h=new HistoryItem(key, json.getInt(key));
			
			spareCopy.add(h);
			add(h);
    }
	}
	
	class HistoryItem {
		String url=null;
		int count=1;
		
		HistoryItem(String url) {
			this.url=url;
		}
		
		HistoryItem(String url, int count) {
			this.url=url;
			this.count=count;
		}
		
		public String toString() {
			return(url);
		}
		
		void emit(JSONStringer json) throws JSONException {
			json.key(url).value(count);
		}
	}
}