package com.skidata.grro.java8lessons.futures;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Java Magazin 04/2015
 * 
 * @author grro
 *
 */
public class FutureTryouts {
	public static String getStockInfo(String company) {
		System.out.println(Thread.currentThread() + "  Entered getStockInfo() " + company);
		String result = "no response";
		String url = "http://finance.google.com/finance/info?client=ig&q=" + company;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		HttpHost proxy = new HttpHost("172.16.10.104", 8080);
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		httpget.setConfig(config);

		try {
			response = httpclient.execute(httpget);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte[] byteResponse = new byte[4096];
					while (instream.read(byteResponse) > 0) {
						result = new String(byteResponse);
					}
				} finally {
					instream.close();
				}
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	static class StockInfos implements Callable<String> {

		private final String company;

		public StockInfos(String company) {
			this.company = company;
		}

		@Override
		public String call() throws Exception {
			System.out.println(Thread.currentThread() + "  Entered call() " + company);
			String result = "no response";
			String url = "http://finance.google.com/finance/info?client=ig&q=" + this.company;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = null;
			HttpHost proxy = new HttpHost("172.16.10.104", 8080);
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			httpget.setConfig(config);

			try {
				response = httpclient.execute(httpget);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					try {
						byte[] byteResponse = new byte[4096];
						while (instream.read(byteResponse) > 0) {
							result = new String(byteResponse);
						}
					} finally {
						instream.close();
					}
				}
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return result;
		}

	}

	public static void main(String[] args) {
		ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(2);
		Future<String> future = executor.submit(new StockInfos("YHOO"));
		Future<String> future2 = executor.submit(() -> getStockInfo("MSFT"));
		Future<String> future3 = executor.submit(new StockInfos("AMZN"));
		Future<String> future4 = executor.submit(new StockInfos("GOOGL"));
		try {
			System.out.println("pull result: " + future.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 2; i++) {
			try {
				System.out.println("poll result " + i + ": " + future.get(0, TimeUnit.NANOSECONDS));
				System.out.println("poll result future 2: " + future2.get(800, TimeUnit.MILLISECONDS));
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		}

		// ############# CompletionService Poll solution ##################

		CompletionService<String> resultQueue = new ExecutorCompletionService<String>(executor);
		resultQueue.submit(new StockInfos("YHOO"));
		resultQueue.submit(() -> getStockInfo("MSFT"));
		resultQueue.submit(new StockInfos("AMZN"));
		resultQueue.submit(new StockInfos("GOOGL"));

		try {
			System.out.println("queue result: " + resultQueue.poll(5000, TimeUnit.MILLISECONDS).get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		// ############# Completable Future Push solution ##################
		// Similar to Pull solution, but in case of an error nothing is printed

		CompletableFuture<String> cFuture = CompletableFuture.supplyAsync(() -> getStockInfo("MSFT"), executor);
		CompletableFuture<String> cFuture2 = CompletableFuture.supplyAsync(() -> getStockInfo("AAPL"), executor);
		CompletableFuture<String> cFuture3 = CompletableFuture.supplyAsync(() -> getStockInfo("DDD"), executor);
		cFuture.thenAccept(info -> System.out.println(Thread.currentThread() + " CFuture result: " + info));
		cFuture2.thenAcceptAsync(info -> System.out.println(Thread.currentThread() + " CFuture result 2: " + info));
		// That is why "PUSH": One result is pushed into another function -->
		// function chain possible
		cFuture3.thenApply(info -> "GEILGEIL " + info).thenAccept(info -> System.out.println("PUSH PUSH " + info));
		executor.shutdown();
	}
}
