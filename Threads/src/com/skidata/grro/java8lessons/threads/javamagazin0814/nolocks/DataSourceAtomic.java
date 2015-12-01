package com.skidata.grro.java8lessons.threads.javamagazin0814.nolocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class DataSourceAtomic {
	final Map<Integer, AtomicReference<CountAndSumFinal>> mapRef = new ConcurrentHashMap<>();

	public void updateLockFree(Integer key, int value) {
		if (mapRef.containsKey(key)) {
			final AtomicReference<CountAndSumFinal> ref = mapRef.get(key);
			CountAndSumFinal prev;
			do {
				prev = ref.get();
			} while (!ref.compareAndSet(prev, prev.withNewValue(value)));
		} else {
			mapRef.put(key, new AtomicReference<>(new CountAndSumFinal(1, value)));
		}
	}

}
