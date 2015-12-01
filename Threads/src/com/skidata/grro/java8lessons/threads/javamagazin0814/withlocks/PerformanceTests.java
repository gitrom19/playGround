package com.skidata.grro.java8lessons.threads.javamagazin0814.withlocks;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class PerformanceTests {
	final int NUM_THREADS = 10;
	final int NUM_KEYS = 2;

	final CountDownLatch latch = new CountDownLatch(NUM_THREADS);

	private final DataSource dataSource;

	public PerformanceTests(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void start() throws InterruptedException {

		for (int i = 0; i < NUM_THREADS; i++) {
			new Thread(new Runnable() {
				final Random random = new Random();

				public void run() {
					for (int i = 0; i < 1_000_000; i++) {
						try {

							for (int j = 0; j < 10; j++) {
								dataSource.getMapSync().get(random.nextInt(NUM_KEYS)).getAverage();
							}
						} catch (NullPointerException e) {
						}
						dataSource.updateSynchronized(random.nextInt(NUM_KEYS), i);
					}
					latch.countDown();
				}
			}).start();
		}
		long start = System.nanoTime();
		latch.await();
		System.out.println((System.nanoTime() - start) + "ns");
	}

}
