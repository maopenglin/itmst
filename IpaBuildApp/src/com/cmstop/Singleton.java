package com.cmstop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Singleton {
	private static Singleton instance;

	private static Map<String, StringBuffer> recored = new HashMap<String, StringBuffer>();

	private Singleton() {
	}

	public static synchronized Singleton getInstance() {
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}

	public synchronized void setRecord(String key, String value) {
		// if(recored.size()<=3){
		// recored.put(key, value);
		StringBuffer buf = recored.get(key);
		if (buf == null) {
			buf = new StringBuffer();
		}
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

		buf.append("<h4>&nbsp;&nbsp;&nbsp;&nbsp;" + value
				+ " <span style=\"font-size:12px\">date:"
				+ formater.format(new Date()) + "</span></h4>");
		recored.put(key, buf);
		// }

	}

	public void clear(String key) {
		recored.remove(key);

	}

	public String getRecord() {

		StringBuffer buffer = new StringBuffer();
		Set set = recored.entrySet();
		java.util.Iterator it = recored.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			buffer.append("<h2>" + entry.getKey() + ":</h2>" + entry.getValue()
					+ "<br/>");
			buffer.append("<p>*******************************************************************************</p>");
		}

		return buffer.toString();
	}
}
