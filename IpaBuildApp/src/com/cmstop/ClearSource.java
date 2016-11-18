package com.cmstop;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class ClearSource extends TimerTask {
	private Timer timer;
	private String cmd;

	public ClearSource(Timer t, String cm) {
		timer = t;
		cmd = cm;
	}

	public void run() {
		Logger log = Logger.getLogger(this.getClass());

		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int iretCode = process.waitFor();
			log.error("删除打包源码:   " + cmd + "  result:" + iretCode);
			System.out.println("删除打包源码:   " + cmd + "  result:" + iretCode);
			timer.cancel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
