package com.skidata.grro.java8lessons.lambdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class LambdaMain {

	public class SBAppend implements Function<String, StringBuilder> {

		@Override
		public StringBuilder apply(String t) {

			return null;
		}

	}

	public static void main(String[] args) {
		List<Integer> intList = new ArrayList<>();
		intList.add(19);
		intList.add(0);
		intList.add(3);
		intList.add(81);

		Utilities.forEach(intList, new Consumer<Integer>() {
			public void accept(Integer elem) {
				System.out.println(elem);
			}
		});

		System.out.println("############ Lambda #############");
		Utilities.forEach(intList, e -> System.out.println(e));

		System.out.println("############ Method reference #############");
		Utilities.forEach(intList, System.out::println);

		System.out.println(
				"############ Collection forEach #############" + Long.toUnsignedString(-2299487823267235085L));
		intList.forEach(e -> System.out.println(e));

		System.out.println("############ Collection forEach #############");
		intList.forEach(Utilities::intOut);

		// intList.sort((Integer a, Integer b) -> Integer.compare(a, b));
		intList.sort((Integer a, Integer b) -> {
			if (a > b)
				return 1;
			else if (a == b)
				return 0;
			else
				return -1;
		});
		System.out.println("############ Method reference sorted #############");
		Utilities.forEach(intList, System.out::println);

		BiPredicate<String, String> sp1 = (s, t) -> s.equals(t);

		System.out.println(sp1.test("Hallo", "Hallo"));

		char[] suffix = new char[] { '.', 't', 'x', 't' };
		Arrays.stream(new String[] { "readme", "releasenotes" }).map(StringBuilder::new).map(s -> s.append(suffix))
				.forEach(System.out::println);

		Stream<String> stringStream = Arrays.stream(new String[] { "fun", "sports" });
		Stream<StringBuilder> sbStream = stringStream.map(new Function<String, StringBuilder>() {
			@Override
			public StringBuilder apply(String t) {
				return new StringBuilder(t);
			}

		});
		Stream<StringBuilder> sbStream2 = sbStream.map(new Function<StringBuilder, StringBuilder>() {

			@Override
			public StringBuilder apply(StringBuilder t) {
				return t.append(suffix);
			}

		});

		for (Iterator<StringBuilder> it = sbStream2.iterator(); it.hasNext();) {
			System.out.println("Old School:" + it.next());
		}
		
		Consumer<Integer> c = (t) -> System.out.println("<"+t+">");
		intList.forEach( c.andThen((x) -> System.out.println("!!!"+x+"!!!")) );
	}
}
