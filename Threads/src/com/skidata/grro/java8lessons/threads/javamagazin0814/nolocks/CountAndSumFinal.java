package com.skidata.grro.java8lessons.threads.javamagazin0814.nolocks;

public class CountAndSumFinal {
	private final int count;
	private final int sum;

	public CountAndSumFinal(int count, int sum) {
		this.count = count;
		this.sum = sum;
	}

	public CountAndSumFinal withNewValue(int value) {
		return new CountAndSumFinal(count + 1, sum + value);
	}

	public double getAverage() {
		if (count == 0) {
			return 0;
		}
		return 1.0 * sum / count;
	}

}
