package com.skidata.grro.java8lessons.futures;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FutureTryouts {
	private class StockInfos implements Callable<String> {

		@Override
		public String call() throws Exception {
			String result = "no response";
			String url = "http://finance.google.com/finance/info";
			String param1 = "ig";
			String param2 = "YHOO";
			String charset = java.nio.charset.StandardCharsets.UTF_8.name();
			String query = String.format("client=%s&q=%s", URLEncoder.encode(param1, charset),
					URLEncoder.encode(param2, charset));
			URLConnection connection = new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			InputStream response = connection.getInputStream();

			byte[] byteResponse = new byte[4096];
			while (response.read(byteResponse) > 0) {
				result = new String(byteResponse);
			}
			response.close();
			return result;
		}

	}

	public static void main(String[] args) {
		Future<String> future;

	}

}
