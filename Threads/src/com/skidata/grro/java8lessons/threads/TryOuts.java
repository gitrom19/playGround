package com.skidata.grro.java8lessons.threads;

import com.skidata.grro.java8lessons.threads.javamagazin0814.withlocks.DataSource;
import com.skidata.grro.java8lessons.threads.javamagazin0814.withlocks.PerformanceTests;

public class TryOuts {

	public static void main(String[] args) {
		PerformanceTests pt = new PerformanceTests(new DataSource());
		try {
			pt.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
