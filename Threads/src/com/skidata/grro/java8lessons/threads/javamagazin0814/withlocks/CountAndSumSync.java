package com.skidata.grro.java8lessons.threads.javamagazin0814.withlocks;

public class CountAndSumSync {
	private int count = 1;
	private int sum;

	public CountAndSumSync(int initialValue) {
		sum = initialValue;
	}

	public synchronized void add(int value) {
		count += 1;
		sum += value;
	}

	public synchronized double getAverage() {
		if (count == 0) {
			return 0;
		}
		return 1.0 * sum / count;
	}
}
