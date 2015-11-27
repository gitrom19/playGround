package com.skidata.grro.java8lessons.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tryouts {

	private static List<String> makeWords(String filename) {
		List<String> words = null;
		try {
			words = Arrays.asList(new String(Files.readAllBytes(Paths.get(filename))).split("[^\\p{L}]"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}

	private static void test(String filename) {
		List<String> words = makeWords(filename);
		words.parallelStream().filter(s -> s.length() > 0).map(String::toUpperCase)
				.forEach(s -> System.out.println(Thread.currentThread().getName() + ": " + s));
	}

	private static void test2(String filename) {
		List<String> words = makeWords(filename);
		String giantStream = words.parallelStream().filter(s -> s.length() > 0).map(String::toUpperCase).reduce("",
				(s1, s2) -> s1 + " " + s2);
		System.out.println(giantStream);
	}

	private static void multipleFor() {
		int dimX = 4;
		int dimY = 4;
		int dimZ = 4;
		Random random = new Random();

		List<List<List<Integer>>> cubicA = new ArrayList<>();

		for (int z = 0; z < dimZ; z++) {
			List<List<Integer>> yList = new ArrayList<>();
			for (int y = 0; y < dimY; y++) {
				List<Integer> xList = new ArrayList<>();
				for (int x = 0; x < dimX; x++) {
					xList.add(random.nextInt());
				}
				yList.add(xList);
			}
			cubicA.add(yList);
		}

		for (List<List<Integer>> yList : cubicA) {
			for (List<Integer> xList : yList) {
				for (Integer integer : xList) {
					System.out.print("  integer=" + integer);
				}
				System.out.println("");
			}
			System.out.println(System.lineSeparator());
		}
	}

	private static void multipleFor1() {
		int dimX = 4;
		int dimY = 4;
		int dimZ = 4;
		Random random = new Random();

		List<List<List<Integer>>> cubicB = new ArrayList<>();

		Stream.generate(() -> Stream.generate(() -> random.ints().limit(dimX).boxed().collect(Collectors.toList()))
				.limit(dimY).collect(Collectors.toList())).limit(dimZ).forEach(cubicB::add);
		
		cubicB.forEach(z -> z.forEach(y -> {System.out.println(); y.forEach(System.out::print);}));

	}

	public static void main(String[] args) {
		String filename = "text.txt";
		test(filename);
		System.out.println("\n\n####################### 1 ######################\n\n");
		test2(filename);
		System.out.println("\n\n####################### 2 ######################\n\n");
		String[] txt = { "State", "of", "the", "Lambda", "Libraries", "Edition" };
		int sum = Arrays.stream(txt).filter(s -> s.length() > 3).peek(System.out::println).map(String::length)
				.peek(System.out::println).reduce(0, Integer::sum);
		System.out.println("\n\n####################### 3 ######################\n\n");
		multipleFor();
		System.out.println("\n\n####################### 4 ######################\n\n");
		multipleFor1();
	}

}
