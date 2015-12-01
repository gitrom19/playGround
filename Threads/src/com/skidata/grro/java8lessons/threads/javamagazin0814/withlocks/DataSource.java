package com.skidata.grro.java8lessons.threads.javamagazin0814.withlocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSource {

	final Map<Integer, CountAndSumSync> mapSync = new ConcurrentHashMap<>();

	public void updateSynchronized(Integer key, int value) {
		if (mapSync.containsKey(key)) {
			mapSync.get(key).add(value);
		} else {
			mapSync.put(key, new CountAndSumSync(value));
		}
	}

	public Map<Integer, CountAndSumSync> getMapSync() {
		return mapSync;
	}
}
