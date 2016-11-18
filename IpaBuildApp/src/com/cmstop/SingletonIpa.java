package com.cmstop;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingletonIpa {
	private static final SingletonIpa instance = new SingletonIpa();
	private static ArrayList<Been> datas = new ArrayList<Been>();

	private static ExecutorService fixedThreadPool = Executors
			.newFixedThreadPool(3);

	// 提供了一个供外部访问本class的静态方法,可以直接访问
	public static SingletonIpa getInstance() {

		return instance;
	}

	public ExecutorService getThread() {

		return fixedThreadPool;
	}

	public int size() {

		return datas.size();
	}

	public Been get(int index) {
		if (datas.size() > index) {
			return datas.get(index);
		} else {

			return null;
		}
	}

	public boolean exist(String client, String siteid, String groupdId,
			String apiurl) {
		for (int i = 0; i < datas.size(); i++) {
			Been target = datas.get(i);
			if (siteid.equals(target.userId)
					&& client.equals(target.getProjectId())
					&& target.getIdentity().equals(groupdId)
					&& target.getApiUrl().equals(apiurl)) {

				return true;
			}

		}

		return false;
	}

	public Been get(String userId, String projectId) {
		Been been = null;
		for (int i = 0; i < datas.size(); i++) {
			Been target = datas.get(i);
			if (userId.equals(target.userId)
					&& projectId.equals(target.getProjectId())) {
				been = target;
				break;
			}

		}
		return been;
	}

	public void addList(Been been) {
		Been b = get(been.userId, been.projectId);
		if (b != null) {
			datas.remove(b);
			datas.add(been);
		} else {
			datas.add(been);
		}
	}

	public void find(Been src) {
		for (int i = 0; i < datas.size(); i++) {
			Been target = datas.get(i);
			if (src.getUserId().equals(target.userId)
					&& src.getProjectId().equals(target.getProjectId())) {

				System.out.println("src name:" + src.displayName
						+ " targetName:" + target.displayName);
			}

		}

	}

	public synchronized void remove(Been been) {
		if (datas.size() > 0) {
			datas.remove(been);
		}

	}
}
